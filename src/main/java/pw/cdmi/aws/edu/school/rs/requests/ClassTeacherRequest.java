package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;

@Data
public class ClassTeacherRequest {

	
	private String teacherId;
	
	private String classId;
	
	private DefaultCourse course;
}
