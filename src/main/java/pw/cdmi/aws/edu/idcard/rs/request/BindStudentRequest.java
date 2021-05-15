package pw.cdmi.aws.edu.idcard.rs.request;

import lombok.Data;

@Data
public class BindStudentRequest {

	private Long sn;
	
	private String classTeamId;
}
