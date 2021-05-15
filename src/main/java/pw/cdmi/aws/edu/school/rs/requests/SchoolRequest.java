package pw.cdmi.aws.edu.school.rs.requests;

import lombok.Data;

import java.util.List;

@Data
public class SchoolRequest {
	
	private String schoolId;
    private String name;            //学校名称
    private String cityId;
    private String districtId;
    private String[] stages;    //覆盖K12学业范围
    private String brief;        //学校简要描述
    
    private String image;
}
