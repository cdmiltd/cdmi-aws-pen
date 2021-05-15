package pw.cdmi.aws.edu.idcard.rs.response;


import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class IDCardRecordResponse {
	
    private String id;

    @JsonFormat(pattern  = "yyyy-MM-dd")
   	private Date createDate;  //生成时间
   	
   	private Long startPageId;  //开始页码
   	
   	private Long endPageId;  //结束页码
   	
   	private Integer sumCount; //生成多少页
   	
   	private Boolean toFile; //是否生成文件
}
