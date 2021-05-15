package pw.cdmi.aws.edu.school.modules.entities;

import java.io.Serializable;

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
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_school_teacher")
public class TeacherEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1323232L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

    private String name;				//姓名

    @Column(name = "sex")
  
    private String sex;					//性别

    @Column(name = "course")
    @Enumerated(EnumType.STRING)
    private DefaultCourse course;		//学科
    
    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private StudyStage stage;			//小学还是初中
    
    private String faceImage;			//头像

    @Column(name = "phone",length=32,unique = true)
    private String phone;        //数字，联系电话
    
    private Integer age;  		//年龄
    
    
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;
    
    private String text;	//简介

    /**
     * 教师对应的学校标识
     */
    @Column(name = "school_id", nullable = false,length = 32)
    private String schoolId;

    
    
    
    
    /**
     * 信息归属应用唯一标识
     */
    @Column(name = "app_id", length = 32)
    private String appId;

    /**
     * 信息归属租户唯一标识
     */
    @Column(name = "tenant_id", length = 32)
    private String tenantId;
    
    
    public String getCourseTitle() {
    	if(course != null) return this.course.getTitle();
    	return null;
    }
    
    public String getStageTitle() {
    	if(this.stage != null) return this.stage.getTitle();
    	return null;
    }

}
