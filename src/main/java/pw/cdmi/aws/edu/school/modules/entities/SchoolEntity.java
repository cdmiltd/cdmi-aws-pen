package pw.cdmi.aws.edu.school.modules.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.SchoolType;
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_school")
public class SchoolEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * 学校名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 学校的机构代码号（营业执照）
     */
    private String licenseNumber;

    /**
     * 学校是否是全日制学校
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SchoolType type;

    /**
     * 学校所在的城市
     */
    @Column(name = "city")
    private String cityCode;

    /**
     * 学校所在的城市区县
     */
    @Column(name = "district")
    private String districtCode;

    /**
     * 学校从事的小学，中学还是大学学业阶段的教育培训
     * 为StudyStage类的Name字段数组
     */
    @Column(name = "stages", length = 64)
    private String stages;

    /**
     * 学校描述信息
     */
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name="brief",columnDefinition="text")
    private String brief;
    
    @Column(name="image",length = 320)
    private String image;
    
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;
    
    /**
     * 管理员 id
     */
    @Column(name="manage_id",length = 32,unique = true)
    private String manageId;
    /**
     * 信息归属应用唯一标识
     */
    @Column(name = "app_id")
    private String appId;
    /**
     * 信息归属租户唯一标识
     */
    @Column(name = "tenant_id")
    private String tenantId;

    public List<String> getStudyStages(){
    	if(StringUtils.isBlank(stages)) return null;
        List<String> stage_names = Arrays.asList(StringUtils.split(stages, ","));
        List<String> list = new ArrayList<>();
        for(String name : stage_names){
            StudyStage stage = StudyStage.fromName(name);
            if(stage != null){
                list.add(stage.getTitle());
            }
        }
        return list;
    }

}
