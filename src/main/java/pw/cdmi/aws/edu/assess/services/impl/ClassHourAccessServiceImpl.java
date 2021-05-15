package pw.cdmi.aws.edu.assess.services.impl;

import java.util.*;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.assess.modules.entities.ClassHourAssess;
import pw.cdmi.aws.edu.assess.repo.ClassHourAssessRepository;
import pw.cdmi.aws.edu.assess.rs.responses.ClasshourStatisticResponse;
import pw.cdmi.aws.edu.assess.services.ClassHourAccessService;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.services.BookClassHourService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Service
public class ClassHourAccessServiceImpl extends BaseServiceImpl<ClassHourAssess, String> implements ClassHourAccessService{

	
	private static final Logger log = LoggerFactory.getLogger(ClassHourAccessServiceImpl.class);
	
	@Autowired
	private ClassHourAssessRepository repo;
	
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
	
	@Override
	public void saveParseData(DrawLineEntities d, StudentEntity student) {
		long pageId = d.getPageNo();
		BookClassHourEntity classHour = classHourService.selectBookClassHourByPageNo(pageId);
		if(classHour == null) {
			log.error("pageId:[{}] 未找到课时信息",pageId);
			return;
		}
		
		
		ClassHourAssess access = repo.getByClasshourIdAndStudentId(classHour.getId(), student.getId());
		if(access == null) {
			access = new ClassHourAssess();
			access.setModifyDate(new Date());
			access.setAssessDate(new Date());
			access.setClasshourId(classHour.getId());
			access.setErrorCount(d.getErrorCount());
			access.setCorrectCount(d.getCorrectCount());
			access.setProblemCount(d.getProblemCount());
			access.setKnowledgeId(classHour.getKnowledges());
			access.setStudentId(student.getId());
			access.setClassTeamId(student.getClassteamid());
			repo.save(access);
		}else {
			access.setCorrectCount(d.getCorrectCount());
			access.setProblemCount(d.getProblemCount());
			access.setKnowledgeId(classHour.getKnowledges());
			access.setModifyDate(new Date());
			repo.saveAndFlush(access);
		}
		
		
		
		
	}

	@Override
	public List<ClassHourAssess> findByClassTeamIdOrderByAssessDateDesc(String classteamId) {
		return repo.findByClassTeamIdOrderByAssessDateDesc(classteamId);
	}

	@Override
	public Integer countByCoursehourIdAndClassteamId(String classhourId, String classteamId) {
		return repo.countByClasshourIdAndClassTeamId(classhourId, classhourId);
	}

	@Override
	public List<ClassHourAssess> findByClasshourIdAndClassTeamId(String classhourId, String classteamId) {
		return repo.findByClasshourIdAndClassTeamIdOrderByAssessDateDesc(classhourId, classteamId);
	}

	@Override
	public List<Map<String, Object>> findByClassteamIdGroupByClasshourId(String classteamId) {
//		String sql = "SELECT classhourId, knowledgeId ,count(*) num FROM edu_classhour_assess eca where eca.classTeamId = '" + classteamId + "' GROUP BY classhourId";
		String sql = "SELECT classhourId, knowledgeId, assessDate ,count(*) num FROM edu_classhour_assess eca where eca.classTeamId = '" + classteamId + "' GROUP BY classhourId ORDER BY assessDate DESC";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}

	@Override
	public List<Map<String, Object>> staticByClassteamIdAndClasshourIdGoupByStudentId(String classteamId, String classhourId) {
//		String sql = "SELECT MAX(errorCount) max, MIN(errorCount) min, avg(errorCount) avg FROM edu_classhour_assess eca where eca.classTeamId = '" + classteamId + "' AND classhourId = '" + classhourId + "' GROUP BY studentId";
		String sql = "SELECT MAX(errorCount + problemCount) max, MIN(errorCount + problemCount) min, ROUND(AVG(errorCount + problemCount), 2) avg FROM edu_classhour_assess eca where eca.classTeamId = '" + classteamId + "' AND classhourId = '" + classhourId + "'";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}

	/**
	 * 统计某班级课时排行
	 * @param classteamId
	 * @param classhourId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> staticRankByClassteamIdAndClasshourIdOrderErrorCount(String classteamId, String classhourId){
//		String sql = "select es.id, es.name, eca1.errorCount FROM  edu_student es left join (select * FROM edu_classhour_assess eca where eca.classhourId = '" + classhourId + "') eca1 on es.id = eca1.studentId where es.class_team_id = '" + classteamId + "' ORDER BY errorCount ASC";
		String sql = "SELECT es.id, es.name, (eca.errorCount + eca.problemCount) errorCount FROM edu_student es, edu_classhour_assess eca where es.id = eca.studentId AND eca.classTeamId = '" + classteamId + "' AND eca.classhourId = '" + classhourId + "' ORDER BY (eca.errorCount + eca.problemCount) ASC";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}

	@Override
	public Integer staticRankClasshourIdAndErrorCount(String classhourId, Integer errorCount) {
		String sql = "SELECT count(*) count FROM edu_classhour_assess eca where eca.classhourId = '" + classhourId + "' AND  eca.errorCount < " + errorCount + " ORDER BY errorCount ASC";
		List<Map<String, Object>> data = queryBySql(sql);
		Integer rank = 1;
		if(!data.isEmpty()){
			rank = (int)Float.parseFloat(String.valueOf(data.get(0).get("count"))) + 1;
		}
		return rank;
	}

	@Override
	public List<Map<String, Object>> staticClassteamClasshourStudy(String classteamId) {
		String sql = "SELECT eca2.assessDate, ebc.subtitle, MAX(eca2.correctCount + eca2.problemCount) max, MIN(eca2.correctCount + eca2.problemCount) min, ROUND(AVG(eca2.correctCount + eca2.problemCount),1) avg FROM (SELECT classhourId,correctCount,problemCount,assessDate from edu_classhour_assess eca where eca.classTeamId = '" + classteamId + "' ORDER BY eca.assessDate DESC) eca2, edu_book_classhour ebc where eca2.classhourId = ebc.id GROUP BY eca2.classhourId LIMIT 7";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}

	@Override
	public List<Map<String, Object>> staticStudentClasshourClassteamRank(String classteamId, String studentId) {
		String sql = "select ebc.id, ebc.subtitle, res.rank, res.assessDate from \n" +
				"(select eca.id, eca.classhourId, (eca.errorCount + eca.problemCount) count, eca.assessDate, eca.studentId,\n" +
				"IF(@pre_classhour_id = eca.classhourId,\n" +
				"   IF(@pre_error_count = (eca.errorCount + eca.problemCount), @cur_rank, @cur_rank \\:= @cur_rank + 1),\n" +
				"   @cur_rank \\:= 1) rank,\n" +
				"@pre_error_count \\:= (eca.errorCount + eca.problemCount),\n" +
				"@pre_classhour_id \\:= eca.classhourId\n" +
				"FROM edu_classhour_assess eca, (SELECT @cur_rank \\:=0, @pre_errorCount = NULL, @pre_classhour_id \\:= NULL) r where eca.classTeamId = '" + classteamId + "'\n" +
				"ORDER BY eca.classhourId, (eca.errorCount + eca.problemCount) asc) res, edu_classhour_assess ea, edu_book_classhour ebc where res.id = ea.id and ebc.Id = ea.classhourId and ea.studentId = '" + studentId + "' order by ea.assessDate desc limit 7";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}

	@Override
	public List<Map<String, Object>> staticStudentClasshourGradeRank(Set<String> classteamIds, String studentId) {
		StringBuilder ids = new StringBuilder("''");
		for (String classteamId: classteamIds) {
			ids.append(",'").append(classteamId).append("'");
		}

		String sql = "select ebc.id, ebc.subtitle, res.rank, res.assessDate from \n" +
				"(select eca.id, eca.classhourId, (eca.errorCount + eca.problemCount) count, eca.assessDate, eca.studentId,\n" +
				"IF(@pre_classhour_id = eca.classhourId,\n" +
				"   IF(@pre_error_count = (eca.errorCount + eca.problemCount), @cur_rank, @cur_rank \\:= @cur_rank + 1),\n" +
				"   @cur_rank \\:= 1) rank,\n" +
				"@pre_error_count \\:= (eca.errorCount + eca.problemCount),\n" +
				"@pre_classhour_id \\:= eca.classhourId\n" +
				"FROM edu_classhour_assess eca, (SELECT @cur_rank \\:=0, @pre_errorCount = NULL, @pre_classhour_id \\:= NULL) r where eca.classTeamId in(" + ids + ")\n" +
				"ORDER BY eca.classhourId, (eca.errorCount + eca.problemCount) asc) res, edu_classhour_assess ea, edu_book_classhour ebc where res.id = ea.id and ebc.Id = ea.classhourId and ea.studentId = '" + studentId + "' order by ea.assessDate desc limit 7";
		List<Map<String, Object>> data = queryBySql(sql);
		return data;
	}

}
