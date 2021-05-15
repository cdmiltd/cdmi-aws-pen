package pw.cdmi.aws.edu.school.modules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 义务制教育标准学制年级枚举
 */
public enum GlobalGradeStage {
//    OneByPreSchool(StudyStage.PreSchool,"幼儿园小班"),
//    TwoByPreSchool(StudyStage.PreSchool,"幼儿园中班"),
//    ThreeByPreSchool(StudyStage.PreSchool,"幼儿园大班"),
    OneByPrimarySchool(StudyStage.PrimarySchool,"小学一年级",1),
    TwoByPrimarySchool(StudyStage.PrimarySchool,"小学二年级",2),
    ThreeByPrimarySchool(StudyStage.PrimarySchool,"小学三年级",3),
    FourByPrimarySchool(StudyStage.PrimarySchool,"小学四年级",4),
    FiveByPrimarySchool(StudyStage.PrimarySchool,"小学五年级",5),
    SixByPrimarySchool(StudyStage.PrimarySchool,"小学六年级",6),
    OneByJuniorSchool(StudyStage.JuniorSchool,"初一",1),
    TwoByJuniorSchool(StudyStage.JuniorSchool,"初二",2),
    ThreeByJuniorSchool(StudyStage.JuniorSchool,"初三",3),
    OneByHighSchool(StudyStage.HighSchool,"高一",1),
    TwoByHighSchool(StudyStage.HighSchool,"高二",2),
    ThreeByHighSchool(StudyStage.HighSchool,"高三",3),
//    OneByCollegeShool(StudyStage.CollegeShool,"大一"),
//    TwoByCollegeShool(StudyStage.CollegeShool,"大二"),
//    ThreeByCollegeShool(StudyStage.CollegeShool,"大三"),
//    FourByCollegeShool(StudyStage.CollegeShool,"大四"),
//    FiveByCollegeShool(StudyStage.CollegeShool,"大五"),
//    OneByGraduateShool(StudyStage.GraduateShool,"研一"),
//    TwoByGraduateShool(StudyStage.GraduateShool,"研二"),
//    ThreeByGraduateShool(StudyStage.GraduateShool,"研三"),
    ;
    private StudyStage stage;
    private String title;
    
    private Integer level;
    
    private final static Map<String, GlobalGradeStage> types = new HashMap<String, GlobalGradeStage>();
    
    static {
    	for (GlobalGradeStage t:GlobalGradeStage.values()) {
			types.put(t.name(), t);
		}
    }
    
    
    public static GlobalGradeStage fromName(String name) {
    	return types.get(name);
    }
    
    GlobalGradeStage(StudyStage stage, String title,Integer level){
        this.stage = stage;
        this.title = title;
        this.level = level;
    }

    public String getTitle(){
        return this.title;
    }

    public StudyStage getStudyStage(){
        return this.stage;
    }
    

    public Integer getLevel() {
		return level;
	}

    
	public static List<GlobalGradeStage> getGradeStages(StudyStage studyStage){
        GlobalGradeStage[] stages = GlobalGradeStage.values();
        return Arrays.stream(stages).filter(t->t.getStudyStage() == studyStage).collect(Collectors.toList());
    }
}
