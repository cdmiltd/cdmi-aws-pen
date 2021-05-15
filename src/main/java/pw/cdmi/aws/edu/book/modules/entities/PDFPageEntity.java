package pw.cdmi.aws.edu.book.modules.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;
import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;

/**
 * pdf 切割
 * @author Administrator
 *
 */
@Data
@Entity(name = "edu_pdf_pages")
@DynamicInsert
@DynamicUpdate   
@ToString
public class PDFPageEntity {

	
	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;              //记录唯一标识
	
	@Column(name = "page_id",unique = true)
	private Long pageId;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private PDFTypeEnum type;		//资源乐类型
	
	
	 @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	    private java.util.Date createDate;
	
	/**
	 * 资源id   
	 * 如果类型是 PDFTypeEnum.TeacherIdCard 资源id 就是班级 id   
	 * 如果类型是 PDFTypeEnum.StudentIdCard 资源id 就是具体学生 id
	 * 如果类型是 PDFTypeEnum.Book		  资源id 就是书籍 id
	 */
	@Column(name = "source_id",length = 32)
	private String sourceId;	
	
	@Column(name="local_path",length = 320)
	private String localPath;	//当前页 本地地址
	
	@Column(name="remote_path",length = 320)
	private String remotePath;	//当前页 远程地址
}
