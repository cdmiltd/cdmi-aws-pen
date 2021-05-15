package pw.cdmi.aws.edu.school.modules;

/**
 * 系统中默认的义务教育的几种学科枚举
 */
public enum DefaultCourse {
    /**语文是从小学1年级到高中三年级**/
    Chinese("语文", new GlobalGradeStage[]{GlobalGradeStage.OneByPrimarySchool,GlobalGradeStage.TwoByPrimarySchool,
            GlobalGradeStage.ThreeByPrimarySchool,GlobalGradeStage.FourByPrimarySchool,
            GlobalGradeStage.FiveByPrimarySchool,GlobalGradeStage.SixByPrimarySchool,
            GlobalGradeStage.OneByJuniorSchool,GlobalGradeStage.TwoByJuniorSchool,
            GlobalGradeStage.ThreeByJuniorSchool,GlobalGradeStage.OneByHighSchool,
            GlobalGradeStage.TwoByHighSchool,GlobalGradeStage.ThreeByHighSchool}),
    /**数学是从小学1年级到高中三年级**/
    Mathematics("数学",new GlobalGradeStage[]{GlobalGradeStage.OneByPrimarySchool,GlobalGradeStage.TwoByPrimarySchool,
            GlobalGradeStage.ThreeByPrimarySchool,GlobalGradeStage.FourByPrimarySchool,
            GlobalGradeStage.FiveByPrimarySchool,GlobalGradeStage.SixByPrimarySchool,
            GlobalGradeStage.OneByJuniorSchool,GlobalGradeStage.TwoByJuniorSchool,
            GlobalGradeStage.ThreeByJuniorSchool,GlobalGradeStage.OneByHighSchool,
            GlobalGradeStage.TwoByHighSchool,GlobalGradeStage.ThreeByHighSchool}),
    /**英语是从小学3年级到高中三年级**/
    English("英语",new GlobalGradeStage[]{GlobalGradeStage.ThreeByPrimarySchool,GlobalGradeStage.FourByPrimarySchool,
            GlobalGradeStage.FiveByPrimarySchool,GlobalGradeStage.SixByPrimarySchool,
            GlobalGradeStage.OneByJuniorSchool,GlobalGradeStage.TwoByJuniorSchool,
            GlobalGradeStage.ThreeByJuniorSchool,GlobalGradeStage.OneByHighSchool,
            GlobalGradeStage.TwoByHighSchool,GlobalGradeStage.ThreeByHighSchool}),
    /**物理是从初中1年级到高中三年级**/
    Physics("物理",new GlobalGradeStage[]{GlobalGradeStage.OneByJuniorSchool,GlobalGradeStage.TwoByJuniorSchool,
            GlobalGradeStage.ThreeByJuniorSchool,GlobalGradeStage.OneByHighSchool,
            GlobalGradeStage.TwoByHighSchool,GlobalGradeStage.ThreeByHighSchool}),
    /**化学是从初中1年级到高中三年级**/
    Chemistry("化学",new GlobalGradeStage[]{GlobalGradeStage.OneByJuniorSchool,GlobalGradeStage.TwoByJuniorSchool,
            GlobalGradeStage.ThreeByJuniorSchool,GlobalGradeStage.OneByHighSchool,
            GlobalGradeStage.TwoByHighSchool,GlobalGradeStage.ThreeByHighSchool}),
    ;

    private String title;
    private GlobalGradeStage[] stages;

    DefaultCourse(String title, GlobalGradeStage[] stages){
        this.title = title;
        this.stages = stages;
    }

    public static DefaultCourse fromName(String name){
        for(DefaultCourse course: DefaultCourse.values()){
            if(course.name().equalsIgnoreCase(name)){
                return course;
            }
        }
        return null;
    }

    public String getTitle(){
        return this.title;
    }

    public GlobalGradeStage[] getStages(){
        return this.stages;
    }
}
