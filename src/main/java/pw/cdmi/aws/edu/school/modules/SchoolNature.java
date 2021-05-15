package pw.cdmi.aws.edu.school.modules;

/**
 * 学校性质枚举
 */
public enum SchoolNature {
    PubliclySchool("公办学校"),
    PrivateSchool("民办学校"),
    ;
    private String title;
    SchoolNature(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
