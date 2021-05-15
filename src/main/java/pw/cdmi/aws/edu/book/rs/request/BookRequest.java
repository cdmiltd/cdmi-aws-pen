package pw.cdmi.aws.edu.book.rs.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;
import pw.cdmi.aws.edu.book.modules.PageSize;
import pw.cdmi.aws.edu.book.modules.TextbookFormat;
import pw.cdmi.aws.edu.book.modules.TextbookSystem;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.Semester;
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
@ToString
public class BookRequest {
	
	@NotNull(message = "书籍名称不能为空")
	@NotBlank(message = "书籍名称不能为空")
	private String name; // 书籍名称

	@NotNull(message = "编号不能为空")
	@NotBlank(message = "编号不能为空")
	private String code; // 书籍编号

	@NotNull(message = "出版社不能为空")
	@NotBlank(message = "出版社不能为空")
	private String publisher; // 出版社
	

	private String image;  //封面图片
	
	private TextbookFormat format; 
	
	@NotNull(message = "教材体系不能为空")
	private TextbookSystem system; // 教材体系
	
	@NotNull(message = "pdf文件不能为空")
	@NotBlank(message = "pdf文件不能为空")
	private String pdfUrl; // 该书pdf文件
	
	@NotNull(message = "学科不能为空")
	private DefaultCourse course; // 学科

	@NotNull(message = "适学阶段不能为空")
    private StudyStage stage; // 适合学部阶段
	
	@NotNull(message = "适合年级不能为空")
	private GlobalGradeStage grade; // 适合年级
	
	@NotNull(message = "学期不能为空")
	private Semester semester; // 选择学期
	
	

	private PageSize pageSize; // 书页大小
	
	private Long startPageno; // 开始页号(点读笔可识别的页号)
	
	private Long endPageno; // 结束页号
	
	
	private String identityRangle; // 学生身份区域，JSON对象，四个物理长度坐标
		
		
	private String scoreRangle; // 得分区域(每页都是在固定的地方)



	
	private String appId;

	
	private String tenantId;
}
