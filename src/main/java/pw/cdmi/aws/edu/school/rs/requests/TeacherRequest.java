package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
public class TeacherRequest {
    private String name;              //老师名称
    
    private DefaultCourse course;		//学科
    
    private StudyStage stage;    //学部
    
    private String sex;                  //老师的性别
    private Integer age;
    private String faceImage;      //老师的头像
    private String phone;     //老师的电话号码
}
