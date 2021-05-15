package pw.cdmi.aws.edu.school.rs.responses;

import java.util.Date;

import javax.persistence.Column;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.StudyTimeUnits;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;

@Data
public class ClassTeamRowResponse {
    private String id;                  //班级ID
    private String schoolId;
    private Integer orderValue;
    private Integer endYear;
    private StudyStage stage;
    private StudyTimeUnits timeunit;
    private Date createdDate;
    private String name;               //班级名称
    private String teacherId;			//班主任 id
    private TeacherEntity adviser;             //班主任
    private Integer studentNum;          //学生数量
    
    private Long idCardSn;   //批改版sn
    
    private String encodeIdCardPdf;   //编码后的 pdf 文件地址
    
    private SchoolDetaiResponse schoolDetail;
    
    public GlobalGradeStage getGradeStage() {
    	return DateUtil.getGrade(endYear, stage);
    }
    
    
    public String getGradeStageTitle() {
    	return getGradeStage().getTitle();
    }
    
    public String getName() {
    	if(StringUtils.isBlank(name)) {
//    		return this.stage.getTitle()+endYear+"级"+orderValue+"班";
    		return this.getGradeStageTitle()+orderValue+"班";
    	}
    	return name;
    }
    
    
    public String getStageTitle() {
    	if(this.stage != null) return this.stage.getTitle();
    	return null;
    }
}
