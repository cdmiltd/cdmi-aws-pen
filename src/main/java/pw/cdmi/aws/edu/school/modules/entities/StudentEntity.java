package pw.cdmi.aws.edu.school.modules.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import pw.cdmi.aws.edu.common.enums.Sex;

/**
 * 平台信息类
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_student")
@Table(indexes = {@Index(name = "class_team_id_sn",columnList = "class_team_id",unique = true),@Index(name = "class_team_id_sn",columnList = "sn",unique = true)})
public class StudentEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 12323L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "userId", length = 36)
    private String userId;

    @Column(name = "name", length = 36)
    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;
    

    /**
     * 学生在班级中的学号
     */
    private Integer sn;

    private String birthday;    //学生的出生日期
    /**
     * 学生当前所在班级
     */
    @Column(name = "class_team_id", length = 36)
    private String classteamid;

    /**
     * 学生所在的学校
     */
    @Column(name = "schoolId", length = 36)
    private String schoolId;
    
    
    private Long cardSn;   //学生身份码
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;

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
