package pw.cdmi.aws.edu.school.rs.responses;

import lombok.Data;

@Data
public class TotalResponse {

	
	private Long schoolTotal;  //学校数量
	
	private Long penTotal; //笔数量
	
	private Long penActivatedTotal; //激活数量
	
	private Long bookTotal; //书本数量
	
}
