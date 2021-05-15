package pw.cdmi.aws.edu.pen.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;
import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.services.PDFPageService;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;
import pw.cdmi.aws.edu.idcard.service.IdCardService;
import pw.cdmi.aws.edu.idcard.service.IdCardStudentService;
import pw.cdmi.aws.edu.pen.modules.PenPoint;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.pen.modules.entities.PenEntities;
import pw.cdmi.aws.edu.pen.repo.PenRepository;
import pw.cdmi.aws.edu.pen.scheduled.DrawLineTask;
import pw.cdmi.aws.edu.pen.service.DrawLineService;
import pw.cdmi.aws.edu.pen.service.PenSerivce;

import java.util.*;

@Service
public class PenServiceImpl extends BaseServiceImpl<PenEntities, String> implements PenSerivce {

	private static final Logger log = LoggerFactory.getLogger(PenServiceImpl.class);

	public static final String Pen_Current_Student_Id = "PEN_CURRENT_STUDENT_ID_%s"; //存入学生编号，key为mac值
	public static final String Redis_Student_Page_Points = "PEN_POINTS_%s_%s";	//临时保存某一个学生某一个课时的批改轨迹
	public static final String Redis_Test_Area_Points = "PEN_POINTS_Test_Area_%s";	//临时保存某一个试卷的区域坐标

	@Autowired
	private PenRepository repo;
	
	@Autowired
	private IdCardService cardService;
	
	@Autowired
	private IdCardStudentService cardStudentService;
	
	@Autowired
	private DrawLineService drawLineService;

	@Autowired
	private RedisUtil redis;
	
	@Autowired
	private PDFPageService pdfPageService;

	@Autowired
	private DrawLineTask drawLineTask;

	@Autowired
	private TextBookService textBookService;

	@Override
	@Transactional
	public int importPen(List<PenEntities> data) {
		List<PenEntities> result = repo.saveAll(data);
		return result.size();
	}

	
	
	/**
	 *  解析同步的笔数据
	 *  有异常抛出,事务回滚 redis数据保存,下次定时任务继续解析
	 *  @return 解析成功记录数, PenPoint  [{1},{2},{3},{4},{5},{6},{7},....]  解析成功 [{1},{2},{3}]  返回 3  其余的不移除 下次从{4}处开始解析  
	 *  0 表示失败
	 */
	@Override
	@Transactional
	public int parseSyncPoints(String mac, List<PenPoint> data) {
		int i = 0;	//已处理同步数据
		for (PenPoint penPoint:data) {
			Long pageId =  penPoint.getPage_id();
			if(pageId == null || pageId == 0){
				log.info("当前轨迹pageId未空,数据没有意义,丢弃数据。pageId:[{}]", pageId);
				i += 1;
				continue;
			}
			PDFPageEntity pageEntity = pdfPageService.getByPageId(pageId);
			
			//根据点位信息中的page_no 判断是否 点击的是批改版
			if(pageEntity != null) {
				//学生批改版
				if(pageEntity.getType() == PDFTypeEnum.StudentIdCard) {
					log.info("当前笔 mac:[{}],是学生批改版studentId:[{}]",mac, pageEntity.getSourceId());
					//如果老师切换学生批改，则统计上一个学生的批改信息
					switchStudent(mac, pageEntity.getSourceId());

					Boolean isSuccess = redis.set(String.format(Pen_Current_Student_Id, mac), pageEntity.getSourceId());
					if(!isSuccess){
						log.error("记录当前笔批改学生信息失败，macId:{}, studentId:{}", mac,pageEntity.getSourceId());
					}
					i += 1;
				}
				//老师批改版 (逻辑和之前一样)
				else if(pageEntity.getType() == PDFTypeEnum.TeacherIdCard) {
					IdCardEntity card =	cardService.getByPageId(pageId);
					float x = penPoint.getConverX();
					float y = penPoint.getConverY();
					log.info("当前页码pageNo:[{}],数据是教师批改版{} ,x:{},y:{}", pageId, card,x,y);
					
					IdCardStudentEntity cardStudent = cardStudentService.selectStudentByRange(card.getId(), x, y);
					if(cardStudent == null) {
						log.warn("老师点击批改版位置错误，获取绑定学生信息失败。macId：{}，cardId：{}，x：{}，y：{}", mac, card.getId(), x, y);
						i += 1;
						continue;
					}
					log.info("当前批改版下标=>{} 对应学生学号{},学生 id:{}",cardStudent.getIndexNum(),cardStudent.getStudentSn(),cardStudent.getStudentId());
					//如果老师切换学生批改，则统计上一个学生的批改信息
					switchStudent(mac, cardStudent.getStudentId());

					Boolean isSuccess = redis.set(String.format(Pen_Current_Student_Id, mac), cardStudent.getStudentId());
					if(!isSuccess){
						log.error("记录当前笔批改学生信息失败，macId:{}, studentId:{}", mac, cardStudent.getStudentId());
					}
					i += 1;
				}
				//其他类型默认为轨迹点
				else{
					String studentId = (String) redis.get(String.format(Pen_Current_Student_Id, mac));
					if (StringUtils.isBlank(studentId)){
						log.warn("当前画笔未选择批改版学生，丢弃当前数据。macId:{}, pageNo:{}, data:{}", mac, pageId, penPoint);
						i += 1;
						continue;
					}

					//跟上次坐标点的x y坐标一样，对画线没有帮助，丢弃该坐标点; "/4"为将1200dpi降为300dpi
					log.info("查询已处理过的页面点位，mac:{}, pageId:{}, studentId:{}", mac, pageId, studentId);
					PenPoint oldPoint = (PenPoint) redis.get(mac + studentId + pageId);
					if(oldPoint != null && oldPoint.getX()/4 == penPoint.getX()/4 && oldPoint.getY()/4 == penPoint.getY()/4){
						i += 1;
						continue;
					}
					//如果是试卷，则只保留区域内的点位
					if(pageEntity.getType() == PDFTypeEnum.Test){
						int[] zb = (int[])redis.get(String.format(Redis_Test_Area_Points, pageEntity.getSourceId()));
						if(zb == null || zb.length == 0){
							TextbookEntity textbookEntity = textBookService.getOne(pageEntity.getSourceId());
							String[] strs = textbookEntity.getScoreRangle().split(",");
							int[] zbs = new int[4];
							for (int m = 0; m < strs.length; m++) {
								zbs[m] = Integer.valueOf(strs[m])*12;
							}
							redis.set(String.format(Redis_Test_Area_Points, pageEntity.getSourceId()), zbs);
						}
						if(penPoint.getX()/4 < zb[0] || penPoint.getX()/4 > zb[2] || penPoint.getY() < zb[1] || penPoint.getY() > zb[3]){
							i += 1;
							continue;
						}
					}
					redis.lpushAll(String.format(Redis_Student_Page_Points, studentId, pageId), penPoint);
					//存入本次轨迹坐标点；与下次做对比
					redis.set(mac + studentId + pageId, penPoint, 600);

					i += 1;
				}
				
			}else {
				//查询不出来的页面信息则直接抛弃
				i += 1;
			}
		}

		return i;
	}

	/**
	 * 切换批改学生，统计上一位学生的所以批改信息
	 * @param mac
	 * @param curStudentId
	 */
	public void switchStudent(String mac, String curStudentId){
		//如果老师切换学生批改，则统计上一个学生的批改信息
		String oldStudentId = (String) redis.get(String.format(Pen_Current_Student_Id, mac));
		log.info("切换当前笔的批改学生，统计上一次学生，mac:[{}], curStudentId:[{}], oldStudentId:[{}]", mac, curStudentId, oldStudentId);
		if(!curStudentId.equals(oldStudentId)){
			//设置新批改学生
			redis.set(String.format(Pen_Current_Student_Id, mac), curStudentId);
			log.info("统计学生所有批改数据，student:[{}]", oldStudentId);
			Set<String> keys = redis.keys(String.format(Redis_Student_Page_Points, oldStudentId, "*"));
			log.info("将上一位批改学生的所有批改数据存入数据库 oldstudentId :[{}]", oldStudentId);
			//设置一个默认学生学号，清除默认学生批改数据
			if("default".equals(oldStudentId)){
				redis.del(keys.toArray(new String[keys.size()]));
			}
			for (String key : keys) {
				log.info("获取上一位批改学生的数据 key :[{}]", key);
				List<Object> list = redis.lGet(key, 0, -1);
				log.info("redis data size =>{}",list.size());
				List<PenPoint> penPoints = ListCopy.copyListProperties(list, PenPoint::new);
				//lpush 添加的 这里需要翻转
				Collections.reverse(penPoints);
				if(penPoints == null || penPoints.size() == 0){
					continue;
				}
				PenPoint penPoint = penPoints.get(0);
				updateDrawLineEntities(mac, penPoint.getPage_id(), oldStudentId, penPoints);
				redis.del(key);
			}
			drawLineTask.executeStudentDrawLine(oldStudentId);
		}
	}

	/**
	 * 更新轨迹数据
	 * @param mac
	 * @param pageId
	 * @param studentId
	 * @param penPoints
	 */
	public void updateDrawLineEntities(String mac, Long pageId, String studentId, List<PenPoint> penPoints){
		DrawLineEntities drawLineEntities = drawLineService.getByPageNoAndStudentId(pageId,studentId);
		if (drawLineEntities == null) {
			drawLineEntities = new DrawLineEntities();
			drawLineEntities.setMac(mac);
			drawLineEntities.setPageNo(pageId);
			JSONArray jsonArray = new JSONArray();
			for (PenPoint penpoint: penPoints) {
				jsonArray.add(penpoint);
			}
			drawLineEntities.setPointDatas(jsonArray.toJSONString());
			drawLineEntities.setCreateTime(new Date());
			drawLineEntities.setStudentId(studentId);
		}else{
			drawLineEntities.setMac(mac);	//存在老师焕笔情况
			JSONArray jsonArray = JSONArray.parseArray(drawLineEntities.getPointDatas());
			for (PenPoint penpoint: penPoints) {
				jsonArray.add(penpoint);
			}
			jsonArray.addAll(penPoints);
			drawLineEntities.setPointDatas(jsonArray.toJSONString());
			drawLineEntities.setUpdateTime(new Date());
		}
		drawLineEntities.setState(Boolean.FALSE);
		drawLineService.save(drawLineEntities);
	}

}
