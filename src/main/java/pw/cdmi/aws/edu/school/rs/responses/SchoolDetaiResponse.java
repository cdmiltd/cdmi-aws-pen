package pw.cdmi.aws.edu.school.rs.responses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import pw.cdmi.aws.edu.region.modules.City;
import pw.cdmi.aws.edu.region.modules.District;
import pw.cdmi.aws.edu.school.modules.SchoolType;
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
public class SchoolDetaiResponse {
    private String id;                  //学校ID
    private String name;                //学校名称
    private String licenseNumber;
    private SchoolType type;
    private String stages;            //拥有的学部
    
    private String manageId;  
    private String mgrName;             //管理员名称
    private String mgrPhone;            //管理员的电话
    private String brief;               //学校简介
    
    private String image;
    
    private String cityCode;

    
    private String districtCode;
    
    private Long penTotal;   //笔数量
    
    
    public String getCityName() {
    	if(StringUtils.isNotBlank(this.cityCode)) return City.valuesOfCode(cityCode).getTitle();
    	
    	return null;
    }
    
    public String getDistrictName() {
    	if(StringUtils.isNotBlank(this.districtCode)) return District.valuesOfCode(districtCode).getTitle();
    	
    	return null;
    }
    
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
