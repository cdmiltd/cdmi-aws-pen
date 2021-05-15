package pw.cdmi.aws.edu.guarder.modules.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 监护人-学生
 * 
 * @author liwenping
 *
 */
@Data
@Entity(name = "edu_guarder_student")
public class GuarderStudentEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", length = 32)
	private String id;

	private String guarderId;
	
	private String studentId;
	
	private String relation;   //关系
	
	
}
