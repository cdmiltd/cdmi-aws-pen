package pw.cdmi.aws.edu.region.rs.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RegionResponse {

	private String code;
	
	private String name;
	
	private String parentCode;
	
	private List<RegionResponse> childs = new ArrayList<>();
}
