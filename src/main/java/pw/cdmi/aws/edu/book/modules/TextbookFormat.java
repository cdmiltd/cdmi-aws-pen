package pw.cdmi.aws.edu.book.modules;

import java.util.HashMap;
import java.util.Map;

/**
 * 载体类别:图书01/试题02
 */
public enum TextbookFormat {
    BOOK("01","图书"),
    TEST("02","试题"),
    ;

	
    private String code;
    private String title;
    
    private final static Map<String, TextbookFormat> types = new HashMap<String, TextbookFormat>();
    static {
    	for (TextbookFormat t:TextbookFormat.values()) {
			types.put(t.name(), t);
		}
    }
    public static TextbookFormat fromName(String name) {
    	return types.get(name);
    }
    
    TextbookFormat(String code, String title){
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
