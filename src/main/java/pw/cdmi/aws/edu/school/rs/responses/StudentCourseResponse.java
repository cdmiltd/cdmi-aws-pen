package pw.cdmi.aws.edu.school.rs.responses;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;

@Data
public class StudentCourseResponse {
    private DefaultCourse course;  //科目编号
    private String courseName;  //科目名称
}
