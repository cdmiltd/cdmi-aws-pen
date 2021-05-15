package pw.cdmi.aws.edu.region.rs;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import pw.cdmi.aws.edu.region.modules.City;
import pw.cdmi.aws.edu.region.modules.District;
import pw.cdmi.aws.edu.region.modules.entites.RegionEntity;
import pw.cdmi.aws.edu.region.rs.response.RegionResponse;
import pw.cdmi.aws.edu.region.service.RegionService;
import pw.cdmi.core.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu", description = "教育模块")
public class RegionResource {

	@Autowired
	private RegionService rs;
    /**
     * 获取当前系统支持的城市列表
     * @return
     */
    @GetMapping("/region/cities")
    public List<RegionResponse> listCity(){
//    	List<RegionEntity> list = rs.findAll();
    	
    	List<RegionResponse> data = new ArrayList<RegionResponse>();
    	
    	for(City c : City.values()) {
    		RegionResponse ee = new RegionResponse();
    		ee.setCode(c.getCode());
    		ee.setName(c.getTitle());
    		List<RegionResponse> childs = new ArrayList<RegionResponse>();
    		List<District> dlist = District.forCityCode(c.getCode());
    		
    		dlist.forEach(e->{
    			RegionResponse t = new RegionResponse();
        		t.setCode(e.getCode());
        		t.setName(e.getTitle());
        		t.setParentCode(c.getCode());
        		childs.add(t);
    		});
    		ee.setChilds(childs);
    		ee.setParentCode("0");
    		data.add(ee);
    	}
    	

    	
    	
    	
        return data;
    }

    
    public static List<RegionResponse> pushManyGroup(List<RegionResponse> list,String pid){  
        List<RegionResponse> arr = new ArrayList<RegionResponse>();  
        for (RegionResponse e : list) {  
            if(pid.equals(e.getParentCode())){  
                e.setChilds(pushManyGroup(list, e.getCode()));  
                arr.add(e); 
            }  
        }  
        return arr;  
    }
    
    /**
     * 获取指定城市下的区县信息
     * @param cityname
     * @return
     */
    @GetMapping("/region/districts")
    public List<RegionResponse> listDistrict(@RequestParam("city") String city){
    	
    	List<RegionResponse> data = new ArrayList<RegionResponse>();
		List<District> dlist = District.forCityCode(city);
		if(dlist == null) return new ArrayList<>();
		
		dlist.forEach(e->{
			RegionResponse t = new RegionResponse();
    		t.setCode(e.getCode());
    		t.setName(e.getTitle());
    		t.setParentCode(city);
    		data.add(t);
		});
        
        return data;
    }
}
