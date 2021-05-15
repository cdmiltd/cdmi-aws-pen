package pw.cdmi.aws.edu.school.modules.entities;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.StudyStage;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 学校课程    直接使用枚举  每个学校  学部(小学,初中) 课程是一样的
 */
@Data
@Deprecated
//@Entity(name = "edu_school_course")
public class SchoolCourseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;

    /**
     * 对应学校唯一标识
     */
    @Column(name = "school_id", nullable = false)
    private String schoolId;

    /**
     * 学科或课程代码(同一个学校不相同，不可以与DefaultCourse中枚举的Name相同)
     */
    @Column(name = "code", length = 24)
    private String code;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "course")
    private DefaultCourse course;   //学科
    
    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private StudyStage stage;    //学部

    /**
     * 学科或课程名称
     */
    @Column(name = "name", length = 24)
    private String name;

    /**
     * 学科或课程描述
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "brief", columnDefinition="text")
    private String brief;
    
    /**
     * 是否启用
     */
    @Column(name = "disable")
    private boolean disable = false;

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
    	if(this.course != null) return this.course.getTitle();
    	return null;
    }
    
    public String getStageTitle() {
    	if(this.stage != null) return this.stage.getTitle();
    	return null;
    }
    
    
}
