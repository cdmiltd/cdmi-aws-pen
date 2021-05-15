package pw.cdmi.aws.edu.region.modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum City {
    Chengdu("01","成都"),
    Chongqing("02","重庆"),
    ;
    private String code;
    private String title;
    
    private static final Map<String, City> data = new HashMap<>();
    
    static {
    	for(City c:City.values()) {
    		data.put(c.code, c);
    	}
    }
    
    public static City valuesOfCode(String code) {
    	return data.get(code);
    }
    
    City(String code, String title){
        this.code = code;
        this.title = title;
    }

    public String getCode(){
        return this.code;
    }
    public String getTitle(){
        return this.title;
    }
}
