package pw.cdmi.aws.edu.book.modules;

/**
 * 试题来源
 */
public enum ExerciseOrigin {
    RealTest("01","考试真题"),
    ;
    private String code;
    private String title;
    ExerciseOrigin(String code, String title){
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
