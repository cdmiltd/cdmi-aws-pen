package pw.cdmi.aws.edu.assess.rs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pw.cdmi.aws.edu.assess.modules.entities.ClassHourAssess;
import pw.cdmi.aws.edu.assess.rs.request.DateRangeRequest;
import pw.cdmi.aws.edu.assess.rs.responses.ClasshourStatisticResponse;
import pw.cdmi.aws.edu.assess.rs.responses.ClassteamClasshourResponse;
import pw.cdmi.aws.edu.assess.rs.responses.KnowledgeStatisticResponse;
import pw.cdmi.aws.edu.assess.services.ClassHourAccessService;
import pw.cdmi.aws.edu.assess.services.KnowledgeAssessService;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.services.BookClassHourService;
import pw.cdmi.aws.edu.book.services.BookKnowLedgerService;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.pen.service.PenSerivce;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.services.ClassTeamService;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/edu/v1")
public class ClassWorkResource {
    @Autowired
    private ClassHourAccessService classHourAccessService;

    @Autowired
    private BookClassHourService bookClassHourService;

    @Autowired
    private TextBookService textBookService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private BookKnowLedgerService bookKnowLedgerService;

    @Autowired
    private KnowledgeAssessService knowledgeAssessService;

    @Autowired
    private ClassTeamService classTeamService;

    /**
     * 统计最近批改课时信息
     * @param classteamId
     * @return
     */
    @GetMapping("/classwork/recently/statistics")
    public Map<String, Object> classworkRecently(@RequestParam("classteamId") String classteamId) {
        if(StringUtils.isBlank(classteamId)) throw new HttpClientException(ErrorMessages.MissRequiredParameter, classteamId);

        Map<String, Object> map = new HashMap<>();
        List<ClassHourAssess> classHourAssesses = classHourAccessService.findByClassTeamIdOrderByAssessDateDesc(classteamId);
        if(classHourAssesses.isEmpty()) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, classteamId);

        ClassHourAssess currentCha = classHourAssesses.get(0);
        map.put("date", currentCha.getAssessDate());
        BookClassHourEntity bkhe = bookClassHourService.getOne(currentCha.getClasshourId());
        if(bkhe == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, currentCha.getClasshourId());
        map.put("classhourName", bkhe.getSubtitle());

        TextbookEntity tbe = textBookService.getOne(bkhe.getBookId());
        if(tbe == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, bkhe.getBookId());

        map.put("coverImage", tbe.getImage());
        map.put("bookName", tbe.getName());

        List<StudentEntity> cteList = studentService.findAllByClassteamid(currentCha.getClassTeamId());
        if(cteList.isEmpty()) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, currentCha.getClassTeamId());
        map.put("totalCount", cteList.size());

//        Integer finishedCount = classHourAccessService.countByCoursehourIdAndClassteamId(currentCha.getClasshourId(), classteamId);
//        if(finishedCount == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, finishedCount);
//        map.put("finishedCount", finishedCount);

        List<ClassHourAssess> list = classHourAccessService.findByClasshourIdAndClassTeamId(currentCha.getClasshourId(), currentCha.getClassTeamId());
        if(list.isEmpty()) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, list);
        map.put("finishedCount", list.size());
        long diff = list.get(0).getAssessDate().getTime() - list.get(list.size()-1).getAssessDate().getTime(); //差值是毫秒级别
        long min = diff/1000; //秒
        String time = "0";
        if(min < 60){
            time = min + "秒";
        }else if(min < 60 * 60 ){
            time = min/60 + "分钟";
        }else{
            time = min/(60 * 60) + "小时";
        }
        map.put("time", time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        map.put("date", list.get(0).getAssessDate()==null?"":dateFormat.format(list.get(0).getAssessDate()));
        return map;
    }

    /**
     * 统计一个班级下指定知识点的课时学习情况
     * @param classteamId
     * @param knowledgeId
     * @return
     */
    @GetMapping("/classwork/statistics")
    public List<ClasshourStatisticResponse> classworkStatistics(@RequestParam("classteamId") String classteamId, @RequestParam("knowledgeId") String knowledgeId) {
        List<ClasshourStatisticResponse> list = knowledgeAssessService.statisticClassteamIdAndKnowledgeIdGroupByChId(classteamId, knowledgeId);
        BookKnowledgeEntity bookKnowledgeEntity = bookKnowLedgerService.getOne(knowledgeId);
        if(bookKnowledgeEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, knowledgeId);
        for (ClasshourStatisticResponse classhourStatisticResponse: list){
            classhourStatisticResponse.setTitle(bookKnowledgeEntity.getTitle());
            BookClassHourEntity bookClassHourEntity = bookClassHourService.getOne(classhourStatisticResponse.getId());
            if(bookClassHourEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, classhourStatisticResponse.getId());
            classhourStatisticResponse.setSortOrderValue(bookClassHourEntity.getOrderValue());
        }
        return list;
    }

    /**
     * 查询一个班级下的所有知识点掌握情况
     * @param classteamId
     * @return
     */
    @GetMapping("/classwork/knowledge/statistics")
    public List<KnowledgeStatisticResponse> classworkKnowStatistics(@RequestParam("classteamId") String classteamId) {
        List<KnowledgeStatisticResponse> list = knowledgeAssessService.statisticByClassteamIdGroupByKnowledgeId(classteamId);
        for (KnowledgeStatisticResponse knowledgeStatisticResponse: list){
            BookKnowledgeEntity bookKnowledgeEntity = bookKnowLedgerService.getOne(knowledgeStatisticResponse.getId());
            if(bookKnowledgeEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, knowledgeStatisticResponse.getId());
            knowledgeStatisticResponse.setTitle(bookKnowledgeEntity.getTitle());
        }
        return list;
    }

    /**
     * 查询一个班级下的所有课时点批改情况
     * @param classteamId
     * @return
     */
    @GetMapping("/classwork/classteam/classhour/correct")
    public List<ClassteamClasshourResponse> findByClassteamIdGroupByClasshourId(@RequestParam("classteamId") String classteamId){
        List<StudentEntity> studentEntities = studentService.findAllByClassteamid(classteamId);
        List<Map<String, Object>> list = classHourAccessService.findByClassteamIdGroupByClasshourId(classteamId);
        List<ClassteamClasshourResponse> ccrList = new ArrayList<>();
        for (Map<String, Object> map: list) {
            ClassteamClasshourResponse classteamClasshourResponse = new ClassteamClasshourResponse();
            classteamClasshourResponse.setId((String) map.get("classhourId"));
            BookKnowledgeEntity bookKnowledgeEntity = bookKnowLedgerService.getOne((String) map.get("knowledgeId"));
            if(bookKnowledgeEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, map.get("knowledgeId"));
            BookClassHourEntity bookClassHourEntity = bookClassHourService.getOne((String) map.get("classhourId"));
            if(bookClassHourEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, (String) map.get("classhourId"));
            classteamClasshourResponse.setClasshourName(bookClassHourEntity.getSubtitle());
            classteamClasshourResponse.setKnowledgeName(bookKnowledgeEntity.getTitle());
            classteamClasshourResponse.setNumber(studentEntities.size() - Integer.valueOf(String.valueOf(map.get("num"))));
            classteamClasshourResponse.setDate((Date) map.get("assessDate"));
            ccrList.add(classteamClasshourResponse);
        }
        return ccrList;
    }

    /**
     * 查询一个班级下指定课时，未批改的学生信息
     * @param classteamId
     * @return
     */
    @GetMapping("/classwork/classteam/noCorrectStudent")
    public List<String> findStudentNameByClassteamIdAndClasshourId(@RequestParam("classteamId") String classteamId, @RequestParam("classhourId") String classhourId){
        if(StringUtils.isBlank(classteamId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, classteamId);
        }
        if(StringUtils.isBlank(classhourId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, classhourId);
        }
        List<StudentEntity> studentEntities = studentService.findAllByClassteamid(classteamId);
        List<ClassHourAssess> list = classHourAccessService.findByClasshourIdAndClassTeamId(classhourId, classteamId);
        List<String> stuList = new ArrayList<>();
        for (StudentEntity student :studentEntities) {
            Boolean flag = false;
            for (ClassHourAssess assess: list){
                if(student.getId().equals(assess.getStudentId())){
                    flag = true;
                }
            }
            if (!flag){
                stuList.add(student.getName());
            }
        }

        return stuList;
    }

    /**
     * 获取指定班级指定课时 最高错误个数、最低错误个数、平均错误个数
     * @param classteamId
     * @param classhourId
     * @return
     */
    @GetMapping("/classwork/classteam/classhour/static")
    public List<Map<String, Object>> staticByClassteamIdAndClasshourIdGoupByStudentId(@RequestParam("classteamId") String classteamId, @RequestParam("classhourId") String classhourId){
        List<Map<String, Object>> data = classHourAccessService.staticByClassteamIdAndClasshourIdGoupByStudentId(classteamId, classhourId);
//        for (Map<String, Object> raw: data) {
//            raw.put("avg", Float.parseFloat(String.valueOf(raw.get("avg"))));
//        }
        return data;
    }

    /**
     * 统计某班级课时排行
     * @param classteamId
     * @param classhourId
     * @return
     */
    @GetMapping("/classwork/classteam/classhour/rank")
    public List<Map<String, Object>> staticRankByClassteamIdAndClasshourIdOrderErrorCount(@RequestParam("classteamId") String classteamId, @RequestParam("classhourId") String classhourId){
        List<Map<String, Object>> list = classHourAccessService.staticRankByClassteamIdAndClasshourIdOrderErrorCount(classteamId, classhourId);
        return list;
    }

    /**
     * 统计某班级最近七次课时最大错误数、最小错误数、平均错误数
     * @param classteamId
     * @return
     */
    @GetMapping("/classwork/classteam/classhour/study")
    public List<Map<String, Object>> staticClassteamClasshourStudy(@RequestParam("classteamId") String classteamId){
        if(StringUtils.isBlank(classteamId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, classteamId);
        }
        List<Map<String, Object>> list = classHourAccessService.staticClassteamClasshourStudy (classteamId);
        return list;
    }

    /**
     * 统计指定班级某一个学生 最近七个知识点错误数量
     * @param studentId
     * @return
     */
    @GetMapping("/classwork/student/knows/study")
    public List<Map<String, Object>> staticStudentKnowsStudy(@RequestParam("studentId") String studentId){
        if(StringUtils.isBlank(studentId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, studentId);
        }
        List<Map<String, Object>> list = knowledgeAssessService.staticStudentKnowsStudy(studentId);
        return list;
    }

    /**
     * 统计指定班级某一个学生 最近七个课时班级排行
     * @param studentId
     * @return
     */
    @GetMapping("/classwork/student/rank")
    public List<Map<String, Object>> staticStudentClasshourClassteamRank(@RequestParam("classteamId") String classteamId,@RequestParam("studentId") String studentId){
        if(StringUtils.isBlank(classteamId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, classteamId);
        }
        if(StringUtils.isBlank(studentId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, studentId);
        }
        SchoolClassTeamEntity classTeamEntity = classTeamService.getOne(classteamId);
        if(classTeamEntity == null){
            throw new HttpClientException(ErrorMessages.NotFoundObjectKey, classteamId);
        }
        //统计班级排名
        List<Map<String, Object>> classteamRank = classHourAccessService.staticStudentClasshourClassteamRank(classteamId, studentId);
        List<SchoolClassTeamEntity> list = classTeamService.findBySchoolIdAndStageAndEndYear(classTeamEntity.getSchoolId(), classTeamEntity.getStage(), classTeamEntity.getEndYear());
        Set<String> set = new HashSet<>();
        for (SchoolClassTeamEntity schoolClassTeamEntity: list){
            set.add(schoolClassTeamEntity.getId());
        }
        //统计班级排名
        List<Map<String, Object>> gradeRank = classHourAccessService.staticStudentClasshourGradeRank(set, studentId);
        List<Map<String, Object>> res = new ArrayList<>();

        for (Map<String, Object> map: classteamRank){
            Map<String, Object> raw = new HashMap<>();
            raw.put("subtitle", map.get("subtitle"));
            raw.put("assessDate", map.get("assessDate"));
            raw.put("classRank", map.get("rank"));
            for (Map<String, Object> map1: gradeRank){
                if (map.get("id").equals(map1.get("id"))){
                    raw.put("gradeRank", map1.get("rank"));
                }
            }
            res.add(raw);
        }
        return res;
    }

}
