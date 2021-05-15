package pw.cdmi.aws.edu.school.modules;

/**
 * 学校类型枚举
 */
public enum SchoolType {

    FullTimeSchool("全日制学校"),
    PartTimeSchool("非全日制学校"),
            ;
    private String title;
    SchoolType(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
