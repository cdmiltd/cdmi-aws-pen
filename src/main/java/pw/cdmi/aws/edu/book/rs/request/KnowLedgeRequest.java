package pw.cdmi.aws.edu.book.rs.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class KnowLedgeRequest {

	@NotNull(message="知识点描述不能为空")
	@NotBlank(message="知识点描述不能为空")
	private String title;               //知识点描述
	
	private Integer sortOrderValue;   
}
