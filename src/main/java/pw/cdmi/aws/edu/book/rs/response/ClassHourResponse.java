package pw.cdmi.aws.edu.book.rs.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class ClassHourResponse {

	private String id;                      //书本课时编号
    private String bookId;                  //书本编号
    private Integer orderValue;              //课时序列号
    
    private String subtitle; 					//课时名称
    
    private Long beginPageNo;
    
    private Long endPageNo;
    
    private String knowledges;				//课时对应的知识点
    
    private KnowledgeResponse know;
   

}
