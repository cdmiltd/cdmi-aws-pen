package pw.cdmi.aws.edu.school.modules;

import java.util.HashMap;
import java.util.Map;

/**
 * 学业阶段枚举
 */
public enum StudyStage {
    PreSchool("学前班",3,""),
    PrimarySchool("小学",6,"Chinese,Mathematics,English"),
    JuniorSchool("初中",3,"Chinese,Mathematics,English,Physics,Chemistry"),
    HighSchool("高中",3,"Chinese,Mathematics,English,Physics,Chemistry"),
    CollegeShool("大学",4,""),       //不支持医学院等5年制或大专3年
    GraduateShool("研究生",3,""),
    ;
    private int year;        //主要是面对中小学计算学习时长
    private String title;
    
    private String courses;  //课程
    
    StudyStage(String title,int year,String courses){
        this.title = title;
        this.year = year;
        this.courses = courses;
    }
    
    private final static Map<String, StudyStage> types = new HashMap<String, StudyStage>();
    static {
    	for (StudyStage t:StudyStage.values()) {
			types.put(t.name(), t);
		}
    }
    public static StudyStage fromName(String name) {
    	return types.get(name);
    }

    public String getTitle(){
        return this.title;
    }
    
    public String getCourses() {
    	return courses;
    }
    
   

    public int getYear(){
        return this.year;
    }
}
