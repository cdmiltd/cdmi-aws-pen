package pw.cdmi.aws.edu.assess.services;

import pw.cdmi.aws.edu.assess.modules.entities.ClassHourAssess;
import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClassHourAccessService extends BaseService<ClassHourAssess, String>{

	
	public void saveParseData(DrawLineEntities drawLineEntities,StudentEntity student);

	/**
	 * 倒叙获取所有课时统计信息
	 * @return
	 */
	List<ClassHourAssess> findByClassTeamIdOrderByAssessDateDesc(String classteamId);

	/**
	 * 统计指定班级的批改数量
	 * @param classhourId
	 * @param classteamId
	 * @return
	 */
	Integer countByCoursehourIdAndClassteamId(String classhourId, String classteamId);

	/**
	 * 查询一个班级所有批改记录
	 * @param classhourId
	 * @param classteamId
	 * @return
	 */
	List<ClassHourAssess> findByClasshourIdAndClassTeamId(String classhourId, String classteamId);

	/**
	 * 统计一个班级的每个课时的批改情况
	 * @param classteamId
	 * @return
	 */
	List<Map<String, Object>> findByClassteamIdGroupByClasshourId(String classteamId);

	/**
	 * 统计指定班级指定课时排名情况
	 * @param classteamId
	 * @return
	 */
	List<Map<String, Object>> staticByClassteamIdAndClasshourIdGoupByStudentId(String classteamId, String classhourId);

	/**
	 * 统计某班级课时排行
	 * @param classteamId
	 * @param classhourId
	 * @return
	 */
	List<Map<String, Object>> staticRankByClassteamIdAndClasshourIdOrderErrorCount(String classteamId, String classhourId);

	/**
	 * 统计某课时的年级总排名数
	 * @param classhourId
	 * @param errorCount
	 * @return
	 */
	Integer staticRankClasshourIdAndErrorCount(String classhourId, Integer errorCount);

	/**
	 * 统计一个班级最近七天课时学习情况
	 * @param classteamId
	 * @return
	 */
	List<Map<String, Object>> staticClassteamClasshourStudy(String classteamId);

	/**
	 * 统计最近七个课时，学生在班级排名
	 * @param classteamId
	 * @param studentId
	 * @return
	 */
	List<Map<String, Object>> staticStudentClasshourClassteamRank(String classteamId, String studentId);

	/**
	 * 统计最近七个课时，学生在年级排名
	 * @param classteamId
	 * @param studentId
	 * @return
	 */
	List<Map<String, Object>> staticStudentClasshourGradeRank(Set<String> classteamId, String studentId);
}
