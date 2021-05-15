package pw.cdmi.aws.edu.school.rs.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuarderResponse {

	private String id;
	
	private String name;
	
	private String phone;
	
	private String relation;
	
	private String studentId;
	
	
}
