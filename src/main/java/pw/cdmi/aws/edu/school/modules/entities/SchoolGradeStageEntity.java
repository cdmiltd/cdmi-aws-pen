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
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.StudyStage;

/**
 * 学校年级
 * @author liwenping   使用枚举
 *
 */
@Data
@DynamicInsert
@DynamicUpdate
@Deprecated
//@Entity(name = "edu_school_grade_stage")
public class SchoolGradeStageEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", length = 32)
	private String id;
	
	
	 /**
     * 对应学校唯一标识
     */
    @Column(name = "school_id", nullable = false)
    private String schoolId;

    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private StudyStage studyStage;
    
    
    @Column(name = "grade_stage")
    @Enumerated(EnumType.STRING)
    private GlobalGradeStage gradeStage;
    /**
     * 学制代码
     */
    @Column(name = "code")
    private String code;
    
    private Integer level;

    
    /**
     * 班级数量
     */
    @Column(name="classNum",columnDefinition="INT default 0",nullable = false)
    private Integer classNum;  
    
    
    
    /**
     * 信息归属应用唯一标识
     */
    @Column(name = "app_id",length = 32)
    private String appId;
    /**
     * 信息归属租户唯一标识
     */
    @Column(name = "tenant_id",length = 32)
    private String tenantId;
}
