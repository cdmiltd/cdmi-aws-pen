package pw.cdmi.aws.edu.assess.services;

import pw.cdmi.aws.edu.assess.modules.entities.KnowledgeAssess;
import pw.cdmi.aws.edu.assess.rs.responses.ClasshourStatisticResponse;
import pw.cdmi.aws.edu.assess.rs.responses.KnowledgeStatisticResponse;
import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;

import java.util.List;
import java.util.Map;

public interface KnowledgeAssessService extends BaseService<KnowledgeAssess, String>{

	
	public void saveParseData(DrawLineEntities drawLineEntities,StudentEntity student);

	/**
	 * 统计指定班级的某一个知识点的课时掌握情况
	 * @param classteamId
	 * @param knowledgeId
	 * @return
	 */
	List<ClasshourStatisticResponse> statisticClassteamIdAndKnowledgeIdGroupByChId(String classteamId, String knowledgeId);

	/**
	 * 统计班级下所有的知识点掌握情况
	 * @param classteamId
	 * @return
	 */
	List<KnowledgeStatisticResponse> statisticByClassteamIdGroupByKnowledgeId(String classteamId);

	/**
	 * 统计某知识点的年级总排名
	 * @param knowledgeId
	 * @param errorCount
	 * @return
	 */
	Integer staticRankKnowledgeIdAndErrorCount(String knowledgeId, Integer errorCount);

	/**
	 * 统计一个班级一个学生，最近七个知识点学习情况
	 * @param studentId
	 * @return
	 */
	List<Map<String, Object>> staticStudentKnowsStudy(String studentId);
}
