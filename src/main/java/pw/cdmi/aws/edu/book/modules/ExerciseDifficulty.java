package pw.cdmi.aws.edu.book.modules;

/**
 * 试题难度
 */
public enum ExerciseDifficulty {
    BaseTrain("01","基础训练"),
    ;
    private String code;
    private String title;
    ExerciseDifficulty(String code, String title){
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
