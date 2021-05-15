package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;
import pw.cdmi.aws.edu.common.enums.Sex;

@Data
public class ExportStudentEntity {

	
	private String name;
	
	private Sex sex;
	
	private String sn;
	
	private String birthday;
	
	private String guarderName;
	
	private String guarderPhone;
	
	private String relation;
	
	private String guarderName2;
	
	private String guarderPhone2;
	
	private String relation2;
}
