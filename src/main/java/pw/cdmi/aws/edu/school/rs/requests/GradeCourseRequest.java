package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;

/**
 * 年级学科
 */
@Data
public class GradeCourseRequest {
    private GlobalGradeStage grade;     //年级
    private String courseCode;          //学科代码
}
