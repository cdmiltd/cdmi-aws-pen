package pw.cdmi.aws.edu.assess.services;

import pw.cdmi.aws.edu.assess.modules.entities.ClassHourAssess;
import pw.cdmi.aws.edu.assess.rs.responses.ClassStudentAssessResponse;
import pw.cdmi.aws.edu.assess.rs.responses.StudentClassHourResponse;
import pw.cdmi.aws.edu.assess.rs.responses.StudentKnowResponse;
import pw.cdmi.aws.edu.assess.rs.responses.SyncAssessResponse;
import pw.cdmi.aws.edu.common.service.BaseService;

import java.util.List;

public interface AssessService extends BaseService<ClassHourAssess, String> {
    public List<ClassStudentAssessResponse> findClassStudentAssess(String schoolId, String classteamId);

    /**
     * 查询某一只笔的批改记录
     * @param mac
     * @return
     */
    public List<SyncAssessResponse> findSyncAssess(String mac);

    /**
     * 统计某学生某课程的课时情况
     * @param userId
     * @param course
     * @return
     */
    List<StudentClassHourResponse> assessClassHourByUserIdAndCourse(String userId, String course);

    /**
     * 统计某学生某课程的知识点情况
     * @param userId
     * @param course
     * @return
     */
    List<StudentKnowResponse> assessKnowByUserIdAndCourse(String userId, String course);

    /**
     * 查询某学生某课时在班级版名
     * @param classHourId
     * @param classTeamId
     * @param errorCount  课时错误个数
     * @return
     */
    Integer assessStuCourseClassHourRank(String classHourId, String classTeamId, Integer errorCount);

    /**
     * 查询某学生某知识点在班级版名
     * @param knowId
     * @param classTeamId
     * @param errorCount  知识点错误个数
     * @return
     */
    Integer assessStuCourseKnowRank(String knowId, String classTeamId, Integer errorCount);
}
