package pw.cdmi.aws.edu.book.rs.request;

import lombok.Data;

@Data
public class BookClassHourRequest {

	private String knowledgeid;
	
	private String subtitle;
	
	private Integer orderValue;
	
	
	private Integer num;
}
