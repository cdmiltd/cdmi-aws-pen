package pw.cdmi.aws.edu.book.rs.request;

import lombok.Data;

@Data
public class DeleteBookRequest {

	
	private String bookId;
	
	private String bookName;
}
