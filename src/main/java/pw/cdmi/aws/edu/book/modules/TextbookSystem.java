package pw.cdmi.aws.edu.book.modules;

/**
 * 教材体系，人教版，北师大版
 */
public enum TextbookSystem {
    WNUE("01","西师大版"),
    PEP("02","人教版"),
    BNUP("03","北师大版"),
    JIANGSU("04","江苏版"),
    ;

    private String code;
    private String title;

    TextbookSystem(String code, String title){
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
