package pw.cdmi.aws.edu.idcard.rs.response;

import lombok.Data;

@Data
public class IdCardStatisticResponse {

	private Long total;
	
	private Long activated;
}
