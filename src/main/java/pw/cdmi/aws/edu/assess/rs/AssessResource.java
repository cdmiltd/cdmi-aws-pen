package pw.cdmi.aws.edu.assess.rs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;
import pw.cdmi.aws.edu.assess.rs.responses.ClassStudentAssessResponse;
import pw.cdmi.aws.edu.assess.rs.responses.StudentClassHourResponse;
import pw.cdmi.aws.edu.assess.rs.responses.StudentKnowResponse;
import pw.cdmi.aws.edu.assess.rs.responses.SyncAssessResponse;
import pw.cdmi.aws.edu.assess.services.AssessService;
import pw.cdmi.aws.edu.assess.services.ClassHourAccessService;
import pw.cdmi.aws.edu.assess.services.KnowledgeAssessService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.idcard.rs.response.IDCardRecordResponse;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/v1")
public class AssessResource {


    @Autowired
    private AssessService assessService;

    @Autowired
    private ClassHourAccessService classHourAccessService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private KnowledgeAssessService knowledgeAssessService;

    @GetMapping("/assess/classhour/{classhourId}/lastweek")
    public List<IDCardRecordResponse> listLastWeekAssessByClassHour(@PathVariable("classhourId") String classhourId,
                                                           @RequestParam("studentId") String studentId) {
        //获取最新7天的指定学生的测评结果
        return null;
    }

    @GetMapping("/assess/knowledge/{knowledgeId}/lastweek")
    public List<IDCardRecordResponse> listLastWeekAssessBy(@PathVariable("knowledgeId") String knowledgeId,
                                                      @RequestParam("studentId") String studentId) {
        //获取最新7天的指定学生的测评结果
        return null;
    }

    /**
     * 查询某班级下所有学生课程成绩情况
     * @param classteamid
     * @param schoolid
     * @return
     */
    @GetMapping("/assess/classteam/grade")
    public List<ClassStudentAssessResponse> ClassStudentAssess(@RequestParam("classteamid") String classteamid, @RequestParam("schoolid")String schoolid) {
        List<ClassStudentAssessResponse> cs = assessService.findClassStudentAssess(schoolid, classteamid);
        return cs;
    }

    /**
     * 查询某一只笔批改数据
     * @param mac
     * @return
     */
    @GetMapping("/assess/sync/mac")
    public List<SyncAssessResponse> ClassStudentAssess(@RequestParam("mac") String mac) {
        List<SyncAssessResponse> sa = assessService.findSyncAssess(mac);
        return sa;
    }

    /**
     * 统计某学生某课程的课时成绩
     * @param userId
     * @param course
     * @return
     */
    @GetMapping("/assess/student/classhour")
    public List<StudentClassHourResponse> assessClassHourByUserIdAndCourse(@RequestParam("userId") String userId, @RequestParam("course") DefaultCourse course) {
        if(StringUtils.isBlank(userId)) throw new HttpClientException(ErrorMessages.MissRequiredParameter, userId);
        if(course == null) throw new HttpClientException(ErrorMessages.MissRequiredParameter, course.getTitle());

        List<StudentClassHourResponse> sa = assessService.assessClassHourByUserIdAndCourse(userId, course.name());
        return sa;
    }

    /**
     * 统计某学生某课程的知识点成绩
     * @param userId
     * @param course
     * @return
     */
    @GetMapping("/assess/student/know")
    public List<StudentKnowResponse> assessKnowByUserIdAndCourse(@RequestParam("userId") String userId, @RequestParam("course") DefaultCourse course) {
        if(StringUtils.isBlank(userId)) throw new HttpClientException(ErrorMessages.MissRequiredParameter, userId);
        if(course == null) throw new HttpClientException(ErrorMessages.MissRequiredParameter, course.getTitle());

        List<StudentKnowResponse> sa = assessService.assessKnowByUserIdAndCourse(userId, course.name());
        return sa;
    }

    /**
     * 统计某学生某课程的课时班级排名
     * @param userId
     * @param classhourId
     * @return
     */
    @GetMapping("/assess/classteam/classhour/rank")
    public Map<String, Integer> assessStuCourseClassHourRank(@RequestParam("userId") String userId, @RequestParam("classhourId") String classhourId, @RequestParam("errorCount") Integer errorCount) {
        if(StringUtils.isBlank(userId)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, userId);
        }
        if(classhourId == null){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, classhourId);
        }
        if(errorCount == null){
            errorCount = 0;
        }
        StudentEntity studentEntity = studentService.getOne(userId);
        if(studentEntity == null){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, userId);
        }
        //班级排名
        Integer classRank = assessService.assessStuCourseClassHourRank(classhourId, studentEntity.getClassteamid(), errorCount);
        Integer gradeRank = classHourAccessService.staticRankClasshourIdAndErrorCount(classhourId, errorCount);
        Map<String, Integer> map = new HashMap<>();
        map.put("classRank", classRank);
        map.put("gradeRank", gradeRank);
        return map;
    }

    /**
     * 统计某学生某课程的知识点班级排名
     * @param userId
     * @param knowledgeId
     * @return
     */
    @GetMapping("/assess/classteam/know/rank")
    public Map<String, Integer> assessStuCourseKnowRank(@RequestParam("userId") String userId, @RequestParam("knowledgeId") String knowledgeId, @RequestParam("errorCount") Integer errorCount) {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(knowledgeId)){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
        if(errorCount == null){
            errorCount = 0;
        }
        StudentEntity studentEntity = studentService.getOne(userId);
        if(studentEntity == null){
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, userId);
        }
        Integer classRank = assessService.assessStuCourseKnowRank(knowledgeId, studentEntity.getClassteamid(), errorCount);
        Integer gradeRank = knowledgeAssessService.staticRankKnowledgeIdAndErrorCount(knowledgeId, errorCount);
        Map<String, Integer> map = new HashMap<>();
        map.put("classRank", classRank);
        map.put("gradeRank", gradeRank);
        return map;
    }

}
