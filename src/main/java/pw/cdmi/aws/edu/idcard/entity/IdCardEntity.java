package pw.cdmi.aws.edu.idcard.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 批改版
 * @author Administrator
 *
 */
@Data
@Entity(name="edu_id_card")
public class IdCardEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1828L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;
	
	
	@Column(name="sn",unique = true)
	private Long sn;  //批改版序列号
	
	@Deprecated
	private String recordId; //批次id
	
	private String schoolId;   //所属学校id
	
	private String classTeamId; //所属班级id
	
	@Column(name="pageId",unique = true)
	private Long pageId;  //页码
	
	
	private Long startPageId;   //上传批编码后的开始页id
	
	private Long endPageId;		
	
	private String pdfPath;   //编码前的pdf地址
	
	@Column(name="pdf_encode_path",length = 1024)
	private String pdfEncodePath;  //编码后的pdf地址
	
	@Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private java.util.Date createDate;
	
	@Transient
	private List<IdCardStudentEntity> dataList;
}
