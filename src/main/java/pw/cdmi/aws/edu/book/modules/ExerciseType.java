package pw.cdmi.aws.edu.book.modules;

/**
 * 试题类型
 */
public enum ExerciseType {
    FillInBlack("01", "填空题"),
    Judge("02", "判断题"),
    SelectOptions("03", "选择题"),
    MultiSelectOptions("04", "多选题"),
    Explain("05", "解答题"),
    ;
    private String code;
    private String title;

    ExerciseType(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
