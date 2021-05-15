package pw.cdmi.aws.edu.school.modules.entities;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.StudyTimeUnits;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 学校学部（小学/初中/高中）
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_school_stage")
public class SchoolStudyStageEntity {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", length = 32)
	private String id;
	
	private String schoolid; // 该班级学制归属学校
	
	private String code; // 学制代码
	
	
	private Integer duration; // 持续学习时间
	
	
	@Enumerated(EnumType.STRING)
	private StudyTimeUnits unit; // 学习时间单位
	
	@Enumerated(EnumType.STRING)
	private StudyStage state;  //学业阶段
	
	
	public String getStageTitle() {
		if(this.state == null) return null;
		return this.state.getTitle();
	}
}
