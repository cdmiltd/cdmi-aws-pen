package pw.cdmi.aws.edu.idcard.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 批改版生成批次
 * @author Administrator
 *
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name="edu_id_card_record")
public class IdCardRecordEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1828L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

	
	private Date createDate;  //生成时间
	
	private Long startPageId;  //开始页码
	
	private Long endPageId;  //结束页码
	
	private Integer sumCount; //生成多少页
	
	private Boolean toFile; //是否生成文件
}
