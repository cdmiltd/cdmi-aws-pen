package pw.cdmi.aws.edu.school.services;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.rs.requests.ClassTeamRequest;

import java.util.List;

public interface ClassTeamService extends BaseService<SchoolClassTeamEntity, String>{
    /**
     * 创建班级
     * @param stage 创建班级对应的年级
     * @param count 该年级内班级数量
     * @return
     */
    void initClassTeam(String schoolid, GlobalGradeStage stage, int count);

    /**
     * 为指定班级设置班主任
     * @param teamId
     * @param teacherId
     */
    void setClassTeamAdviser(String teamId, String teacherId);

    /**
     * 编辑指定班级信息
     */
    void editClassTeam(String id, String name, String teacherId);

    /**
     * 删除一个班级信息
     */
    void delectClassTeam(String id);

    /**
     * 批量删除已选择的班级信息
     */
    void batchDelectClassTeam(String[] ids);


	void createClassTeam(String schoolid, ClassTeamRequest req);

    /**
     * 查询一个年级下所有班级
     * @param stage
     * @param endYear
     * @return
     */
	List<SchoolClassTeamEntity> findBySchoolIdAndStageAndEndYear(String schoolId, StudyStage stage, Integer endYear);
}
