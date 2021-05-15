package pw.cdmi.aws.edu.book.rs.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class KnowledgeResponse {

	 private String id;                  //知识点编号
	    private String bookId;              //书本编号
	    private String title;               //知识点描述
	    private Integer sortOrderValue;     //排序字段
	    private String code;                //知识点编码
	    
	    private String pages;               //知识点对应的教材页码数组
	    
	    private String classhours;          //知识点对应的课时序列数组

	    /**
	     * 获取教材中知识点对应的课时序列列表
	     * @return
	     */
	    public List<String> getClassHours(){
	        if(StringUtils.isBlank(this.classhours)){
	            return null;
	        }else{
	            List<String> chournums = new ArrayList<>();
	            String[] chour_nums = this.classhours.split(",");
	            for(String chour_num : chour_nums){
	                chournums.add(chour_num);
	            }
	            return chournums;
	        }
	    }
}
