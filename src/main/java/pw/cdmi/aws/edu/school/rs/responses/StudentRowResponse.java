package pw.cdmi.aws.edu.school.rs.responses;

import java.util.List;

import lombok.Data;
import pw.cdmi.aws.edu.common.enums.Sex;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;

@Data
public class StudentRowResponse {
	private String id;

	private String userId;

	private String name;

	private Sex sex;

	/**
	 * 学生在班级中的学号
	 */
	private Integer sn;

	private String birthday; // 学生的出生日期

	private String classteamid;

	private String classteamName;	//班级名称 如 小学3年级2班

	private String schoolId;

	private String schoolName; 	//学校名称
	
	private List<GuarderResponse> guarders;

}
