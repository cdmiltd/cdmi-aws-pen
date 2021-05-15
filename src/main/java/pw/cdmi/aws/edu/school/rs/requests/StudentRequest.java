package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;
import pw.cdmi.aws.edu.common.enums.Sex;

@Data
public class StudentRequest {
	
	private String classteamid;
    private String name;        //学生的名称
    private Sex sex;         //学生的性别
    private Integer sn;          //学生在班级中的班号
    
    
    private String birthday;    //学生的出生日期
    
    private GuarderRequest guarder;
}
