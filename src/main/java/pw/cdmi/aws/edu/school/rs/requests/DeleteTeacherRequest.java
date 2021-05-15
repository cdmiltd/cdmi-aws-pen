package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;

@Data
public class DeleteTeacherRequest {

	
	private String teacherId;
	
	private String name;
}
