package pw.cdmi.aws.edu.assess.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.assess.modules.entities.KnowledgeAssess;
import pw.cdmi.aws.edu.assess.repo.KnowledgeAssessRepository;
import pw.cdmi.aws.edu.assess.rs.responses.ClassStudentAssessResponse;
import pw.cdmi.aws.edu.assess.rs.responses.ClasshourStatisticResponse;
import pw.cdmi.aws.edu.assess.rs.responses.KnowledgeStatisticResponse;
import pw.cdmi.aws.edu.assess.services.KnowledgeAssessService;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.services.BookClassHourService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class KnowledgeAssessServiceImpl extends BaseServiceImpl<KnowledgeAssess, String> implements KnowledgeAssessService{

	private static final Logger log = LoggerFactory.getLogger(KnowledgeAssessServiceImpl.class);
	
	@Autowired
	private KnowledgeAssessRepository repo;
	
	@Autowired
	private BookClassHourService classHourService;

	@PersistenceContext
	EntityManager em;

	public List<Map<String,Object>> queryBySql(String sql){
		Query query = em.createNativeQuery(sql);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}
	
	/**
	 * 保存解析的 勾 差
	 */
	public void saveParseData(DrawLineEntities d,StudentEntity student) {
		
		long pageId = d.getPageNo();
		BookClassHourEntity classHour = classHourService.selectBookClassHourByPageNo(pageId);
		if(classHour == null) {
			log.error("pageId:[{}] 未找到课时信息",pageId);
			return;
		}
		
		KnowledgeAssess access = repo.getByKnowledgeIdAndStudentId(classHour.getKnowledges(), student.getId());
		if(access == null) {
			access = new KnowledgeAssess();
			access.setAssessDate(new Date());
			access.setModifyDate(new Date());
			access.setClassTeamId(student.getClassteamid());
			access.setErrorCount(d.getErrorCount());
			access.setCorrectCount(d.getCorrectCount());
			access.setKnowledgeId(classHour.getKnowledges());
			access.setProblemCount(d.getProblemCount());
			access.setStudentId(student.getId());
			repo.save(access);
		}else {
			access.setModifyDate(new Date());
			access.setErrorCount(d.getErrorCount());
			access.setCorrectCount(d.getCorrectCount());
			access.setProblemCount(d.getProblemCount());
			repo.saveAndFlush(access);
		}
		
	}

	@Override
	public List<ClasshourStatisticResponse> statisticClassteamIdAndKnowledgeIdGroupByChId(String classteamId, String knowledgeId) {
		String sql = "SELECT classhourId,MAX(errorCount + problemCount) max,MIN(errorCount + problemCount) min,ROUND(AVG(errorCount + problemCount), 2) avg FROM edu_classhour_assess eca where eca.classTeamId = '" + classteamId + "' and knowledgeId = '" + knowledgeId + "' GROUP BY classhourId";
		List<ClasshourStatisticResponse> list = new ArrayList<>();
		List<Map<String, Object>> data = queryBySql(sql);
		if(!data.isEmpty()){
			for (Map<String, Object> raw: data){
				ClasshourStatisticResponse cs = new ClasshourStatisticResponse();
				cs.setId((String) raw.get("classhourId"));
				cs.setMax((int)Float.parseFloat(String.valueOf(raw.get("max"))));
				cs.setMin((int)Float.parseFloat(String.valueOf(raw.get("min"))));
				cs.setAverage(Float.parseFloat(String.valueOf(raw.get("avg"))));
				list.add(cs);
			}
		}
		return list;
	}

	@Override
	public List<KnowledgeStatisticResponse> statisticByClassteamIdGroupByKnowledgeId(String classteamId) {
//		String sql = "SELECT knowledgeId,MAX(errorCount + problemCount) max,MIN(errorCount + problemCount) min,ROUND(AVG(errorCount + problemCount), 2) avg FROM edu_knowledge_assess where classTeamId = '" + classteamId + "' GROUP BY knowledgeId";
		String sql = "SELECT knowledgeId, assessDate ,MAX(errorCount + problemCount) max,MIN(errorCount + problemCount) min,ROUND(AVG(errorCount + problemCount), 2) avg FROM edu_knowledge_assess where classTeamId = '" + classteamId + "' GROUP BY knowledgeId ORDER BY assessDate DESC";
		List<KnowledgeStatisticResponse> list = new ArrayList<>();
		List<Map<String, Object>> data = queryBySql(sql);
		if(!data.isEmpty()){
			for (Map<String, Object> raw: data){
				KnowledgeStatisticResponse cs = new KnowledgeStatisticResponse();
				cs.setId((String) raw.get("knowledgeId"));
				cs.setMax((int)Float.parseFloat(String.valueOf(raw.get("max"))));
				cs.setMin((int)Float.parseFloat(String.valueOf(raw.get("min"))));
				cs.setAvg(Float.parseFloat(String.valueOf(raw.get("avg"))));
				cs.setDate((Date)raw.get("assessDate"));
				list.add(cs);
			}
		}
		return list;
	}

	@Override
	public Integer staticRankKnowledgeIdAndErrorCount(String knowledgeId, Integer errorCount) {
		String sql = "SELECT COUNT(*) count FROM edu_knowledge_assess eka where eka.knowledgeId = '" + knowledgeId + "' AND eka.errorCount < " + errorCount + " ORDER BY eka.errorCount ASC";
		List<Map<String, Object>> data = queryBySql(sql);
		Integer rank = 1;
		if(!data.isEmpty()){
			rank = (int)Float.parseFloat(String.valueOf(data.get(0).get("count"))) + 1;
		}
		return rank;
	}

	@Override
	public List<Map<String, Object>> staticStudentKnowsStudy(String studentId) {
		String sql = "SELECT eka.assessDate ,ebk.title ,(eka.errorCount + eka.problemCount) errorCount FROM edu_knowledge_assess eka, edu_book_knowledge ebk where eka.knowledgeId = ebk.Id AND eka.studentId = '" + studentId + "' ORDER BY eka.assessDate DESC limit 7";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}
}
