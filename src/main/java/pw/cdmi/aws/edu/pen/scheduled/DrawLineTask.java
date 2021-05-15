package pw.cdmi.aws.edu.pen.scheduled;

import com.alibaba.fastjson.JSONArray;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pw.cdmi.aws.edu.assess.modules.entities.ScoreAssess;
import pw.cdmi.aws.edu.assess.services.ClassHourAccessService;
import pw.cdmi.aws.edu.assess.services.KnowledgeAssessService;
import pw.cdmi.aws.edu.assess.services.ScoreAssessService;
import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;
import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;
import pw.cdmi.aws.edu.book.services.PDFPageService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.pen.modules.PenPoint;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.pen.service.DrawLineService;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.*;

@Component
public class DrawLineTask {
    private static final Logger log = LoggerFactory.getLogger(DrawLineTask.class);

    @Autowired
    private DrawLineService drawLineService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private KnowledgeAssessService knowAccessService;

    @Autowired
    private ClassHourAccessService chAccessService;

    @Autowired
    private PDFPageService pdfPageService;

    @Autowired
    private ScoreAssessService scoreAssessService;

    @Value("${draw.image.path}")
    private String imagePath = "/opt/edu/temp/";

    @Value("${draw.orc.tessreact.path}")
    private String tessreactPath = "/opt/edu/tessreact/";

    /**
     * @Scheduled(fixedRate=3000)：上一次开始执行时间点后3秒再次执行；
     @Scheduled(fixedDelay=3000)：上一次执行完毕时间点3秒再次执行；
     @Scheduled(initialDelay=1000, fixedDelay=3000)：第一次延迟1秒执行，然后在上一次执行完毕时间点3秒再次执行；
     @Scheduled(cron="* * * * * ?")：按cron规则执行；
     */
    @Scheduled(initialDelay=1000, fixedDelay=30 * 60 * 1000)
    public void drawLineTasks() {
        log.info("根据页面坐标点，生成老师批改轨迹");
        List<DrawLineEntities> drawLineEntitiesList = drawLineService.findListByState(Boolean.FALSE);
        if(drawLineEntitiesList.isEmpty()){
            log.info("没有需要画的轨迹");
        }
        for (DrawLineEntities drawLineEntities: drawLineEntitiesList) {
            dealWithDrawLine(drawLineEntities);
        }

    }

    /**
     * 获取单个学生某一个课时分数
     * @param studentId
     */
    public void executeStudentDrawLine(String studentId){
        List<DrawLineEntities> drawLineEntitiesList = drawLineService.findListByStudentIdAndState(studentId, Boolean.FALSE);
        if(drawLineEntitiesList.isEmpty()){
            log.warn("指定统计学生错误个数信息失败，学生课时批改轨迹不存在， studentId:[{}]", studentId);
        }
        for (DrawLineEntities drawLineEntities: drawLineEntitiesList) {
            dealWithDrawLine(drawLineEntities);
        }

    }

    //处理批改轨迹
    public void dealWithDrawLine(DrawLineEntities drawLineEntities){
        List<PenPoint> penPointList = JSONArray.parseArray(drawLineEntities.getPointDatas(), PenPoint.class);
        //画图
        drawUserTravelImage(drawLineEntities.getStudentId(), penPointList);

        PDFPageEntity pageEntity = pdfPageService.getByPageId(drawLineEntities.getPageNo());
        if(pageEntity.getType() == PDFTypeEnum.Test){
            discernScore(drawLineEntities);
        }else {
            //识别勾勾叉叉数量
            discernCorrectOrError(drawLineEntities);
        }
    }

    //根据单页面轨迹点画图
    public void drawUserTravelImage(String studentId,List<PenPoint> pointData){
        if(pointData.isEmpty()){
            return;
        }
        //毫米转成300dpi像素点
        int width = (int)(pointData.get(0).getPage_width() * 12);
        int height = (int)(pointData.get(0).getPage_height() * 12);
        //声明初始化画布（图片的缓冲区）
        BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        //通过画布获取画笔
        Graphics paint = bufferedImage.getGraphics();
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        //填充图片背景色为白色
        paint.fillRect(0, 0, width, height);
        //设置画笔的颜色为红色，为绘制图片内容作准备
        paint.setColor(Color.RED);

        List<PenPoint> childPenPointList = new ArrayList<>();
        for (int i = 0; i < pointData.size(); i++){
            if (pointData.get(i).getStroke_start()){
                drawLineByPoint(paint, childPenPointList);
                childPenPointList = new ArrayList<>();
            }
            //对笔传入的x或者y坐标为0的值，抛弃掉
            if(pointData.get(i).getX() != 0 && pointData.get(i).getY() != 0){
                childPenPointList.add(pointData.get(i));
            }
            if(i == pointData.size() - 1){
                drawLineByPoint(paint, childPenPointList);
            }
        }

        //绘制完成，则将现在缓冲区的图片样本保存在本地文件中
        try {
            String folderPath = imagePath + studentId;
            File file = new File(folderPath);
            if(!file.exists()){
                file.mkdir();
            }
            ImageIO.write(bufferedImage, "jpeg", new FileOutputStream( folderPath + "/" + pointData.get(0).getPage_id() + ".jpg"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //根据点位坐标画成线条
    public void drawLineByPoint(Graphics paint, List<PenPoint> penPoints){
        if(penPoints.isEmpty()){
            return;
        }
        PenPoint currentPenPoint = penPoints.get(0);

        for (int i = 1; i < penPoints.size(); i++){
            PenPoint penPoint = penPoints.get(i);
            paint.drawLine(currentPenPoint.getX()/4,currentPenPoint.getY()/4,penPoint.getX()/4,penPoint.getY()/4);
            currentPenPoint = penPoint;
        }
    }

    //识别正确错误数量
    public void discernCorrectOrError(DrawLineEntities drawLineEntities){
        Map<String, String> map1 = System.getenv();
        String prefix = map1.get("TESSDATA_PREFIX");
        System.out.println(prefix);

        String filePath = imagePath + "/" + drawLineEntities.getStudentId() + "/" + drawLineEntities.getPageNo() + ".jpg";
        File imageFile = new File(filePath);
        log.info("识别文件：{}，文件是否存在:{}" , filePath , imageFile.exists());
        Tesseract tessreact = new Tesseract();
        tessreact.setTessVariable("user_defined_dpi", "300");
        //需要指定训练集
        tessreact.setDatapath(tessreactPath);
        //默认是英文识别，如果做中文识别，需要单独设置。
        tessreact.setLanguage("cor_err");
        try {
            String result = tessreact.doOCR(imageFile);
            Map<String, Integer> map = correctCountByStr(result);
            drawLineEntities.setCorrectCount(map.get("correctCount"));
            drawLineEntities.setErrorCount(map.get("errorCount"));
            drawLineEntities.setProblemCount(map.get("problemCount"));
            drawLineEntities.setStatisTime(new Date());
            drawLineEntities.setState(Boolean.TRUE);
            drawLineService.saveAndFlush(drawLineEntities);

            StudentEntity student = studentService.getOne(drawLineEntities.getStudentId());
            if(student != null) {
            	//按照课时统计 1、查询该学生课时点是否已经统计过。 2、更新当前统计数据
                knowAccessService.saveParseData(drawLineEntities, student);
             
                //按照知识点统计 1、查询该学生知识点是否已经统计过。 2、更新当前统计数据
                chAccessService.saveParseData(drawLineEntities, student);
            }
          

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    //识别分数
    public void discernScore(DrawLineEntities drawLineEntities){
        Map<String, String> map1 = System.getenv();
        String prefix = map1.get("TESSDATA_PREFIX");
        System.out.println(prefix);

        String filePath = imagePath + "/" + drawLineEntities.getStudentId() + "/" + drawLineEntities.getPageNo() + ".jpg";
        File imageFile = new File(filePath);
        log.info("识别文件：{}，文件是否存在:{}" , filePath , imageFile.exists());
        Tesseract tessreact = new Tesseract();
        tessreact.setTessVariable("user_defined_dpi", "300");
        //需要指定训练集
        tessreact.setDatapath(tessreactPath);
        //默认是英文识别，如果做中文识别，需要单独设置。
        tessreact.setLanguage("cor_num");
        try {
            String result = tessreact.doOCR(imageFile);
            Integer score = scoreByStr(result);

            drawLineEntities.setStatisTime(new Date());
            drawLineEntities.setState(Boolean.TRUE);
            drawLineService.saveAndFlush(drawLineEntities);

            StudentEntity student = studentService.getOne(drawLineEntities.getStudentId());
            PDFPageEntity pageEntity = pdfPageService.getByPageId(drawLineEntities.getPageNo());
            if(student != null && pageEntity != null) {
                //按照课时统计 1、查询该学生课时点是否已经统计过。 2、更新当前统计数据
                ScoreAssess scoreAssess = scoreAssessService.getByTextBookIdAndStudentId(pageEntity.getSourceId(), student.getId());
                if(scoreAssess == null){
                    scoreAssess = new ScoreAssess();
                    scoreAssess.setStudentId(student.getId());
                    scoreAssess.setScore(score);
                    scoreAssess.setAssessDate(new Date());
                    scoreAssess.setTextBookId(pageEntity.getSourceId());
                    scoreAssess.setClassTeamId(student.getClassteamid());
                }else {
                    scoreAssess.setScore(score);
                    scoreAssess.setModifyDate(new Date());
                }
                scoreAssessService.save(scoreAssess);
            }

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    //根据字符串计算勾勾叉叉数量
    public Map<String, Integer> correctCountByStr(String corErrStr) {
        corErrStr = corErrStr.replaceAll("\\s*", "");
        int correctCount = 0;
        int errorCount = 0;
        int problemCount = 0;
        char[] charArray = corErrStr.toCharArray();
        for(int i=0; i< corErrStr.length(); i++)
        {
            if('v' == charArray[i]){
                correctCount += 1;
            }else if('x' == charArray[i]){
                errorCount += 1;
            }else if('k' == charArray[i]){
                problemCount += 1;
            }
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("correctCount", correctCount);
        map.put("errorCount", errorCount);
        map.put("problemCount", problemCount);
        return map;
    }

    //根据字符串识别分数
    public Integer scoreByStr(String scoreStr) {
        Integer score = 0;
        scoreStr = scoreStr.replaceAll("\\s*", "");
        try {
            score = Integer.valueOf(scoreStr);
        }catch (Exception e){
            log.error("识别学生试卷分数失败, 识别字符串为：[{}]，失败原因：{}", scoreStr, e.getMessage());
        }
        return score;
    }

}
