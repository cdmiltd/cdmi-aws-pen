package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;
import lombok.ToString;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;

@Data
@ToString
public class ClassTeamRequest {
	
	private GlobalGradeStage grade;     //年级
	
	private Integer count;				//数量
    private Integer orderValue;             //班级序号
    private String name;               //班级标题
    private String adviserId;           //指定班主任
    
    private String schoolId;
}
