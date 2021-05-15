package pw.cdmi.aws.edu.guarder.modules.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * 表连接查询 无意义
 * @author Administrator
 *
 */
@Data
@Entity(name = "tmp_innerjoin_guarder_student")
public class GuarderInfo {

	@Id
	 @Column(name = "id", length = 32)
	private String id;
	
	private String name;
	
	private String phone;
	
	private String relation;
	
	private String studentId;
}
