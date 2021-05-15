package pw.cdmi.aws.edu.school.modules.entities;

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
import pw.cdmi.aws.edu.school.modules.DefaultCourse;

/**
 * 班级对应的学科老师
 * 
 * @author liwenping
 *
 */

@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_calss_teacher")
public class ClassTeacherEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", length = 32)
	private String id;
	
	/**
	 * 学校编号
	 */
	private String schoolId;
	
	/**
	 * 老师编号
	 */
	private String teacherId;
	
	/**
	 * 班级 id
	 */
	private String classId;
	
	 @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	   private java.util.Date createDate;
	
	/**
	 * 老师负责学科 冗余字段
	 */
	@Column(name = "course")
	@Enumerated(EnumType.STRING)
	private DefaultCourse course;
	
}
