package pw.cdmi.aws.edu.book.rs.request;

import lombok.Data;

@Data
public class CreateClassHourRequest {
	
	private String id;

    private String bookId;                  //书本编号
    
    
    private Integer orderValue;              //课时序列号
    
    private String subtitle; 					//课时名称
    
    private Long beginPageNo;
    
    private Long endPageNo;
    
    private String knowledges;				//课时对应的知识点
}
