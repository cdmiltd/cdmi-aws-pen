package pw.cdmi.aws.edu.book.rs.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import pw.cdmi.aws.edu.book.modules.PageSize;

@Data
public class BookTestPaperRequest {

	
	
	private String bookId;
	
	private String testBookId;
	
	@NotNull(message = "pdf文件不能为空")
	@NotBlank(message = "pdf文件不能为空")
	private String pdfUrl;
	
	@NotNull(message = "页码大小不能为空")
	private PageSize pageSize;
	
	@NotNull(message = "开始页号不能为空")
	private Long startPageno; // 开始页号(点读笔可识别的页号)
	
	@NotNull(message = "结束页号不能为空")
	private Long endPageno; // 结束页号
	
	@NotNull(message = "学生身份区域坐标不能为空")
	@NotBlank(message = "学生身份区域坐标不能为空")
	private String identityRangle; // 学生身份区域，JSON对象，四个物理长度坐标
	
	@NotNull(message = "得分区域坐标不能为空")
	@NotBlank(message = "得分区域坐标不能为空")
	private String scoreRangle; // 得分区域(每页都是在固定的地方)
}
