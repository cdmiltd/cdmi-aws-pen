package pw.cdmi.aws.edu.book.rs.request;

import lombok.Data;

@Data
public class ClassHourPagesRequest {

	private String id;
	
	private String[] pages;
}
