package pw.cdmi.aws.edu.idcard.rs.response;

import lombok.Data;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
public class IdCardNewFiveResponse {

	
	private String id;
	
	private String schoolId;
	
	private String schoolName;
	
	private String stage;
	
	private Long  idCardSn;
	
	private Integer endYear;
	
	private Integer orderValue;
	
	private String idCardPdfPath;
	
	public GlobalGradeStage getGradeStage() {
    	return DateUtil.getGrade(endYear, StudyStage.valueOf(stage));
    }
    
    
    public String getGradeStageTitle() {
    	return getGradeStage().getTitle();
    }
    
    public String getName() {
    	
    	return this.getGradeStageTitle()+orderValue+"班";
    	
    }
}
