package pw.cdmi.aws.edu.school.modules;

/**
 * 学期（上半学期，下半学期）枚举
 */
public enum Semester {
    First("上学期"),
    Second("下学期"),
    ;
    private String title;
    Semester(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
