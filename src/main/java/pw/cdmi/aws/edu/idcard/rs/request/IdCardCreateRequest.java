package pw.cdmi.aws.edu.idcard.rs.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IdCardCreateRequest {

	private Integer count;
	
	private Long startPageId;
	
	private Long endPageId;
}
