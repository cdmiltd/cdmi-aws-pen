package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;

/**
 * 教师端-批改同步数据
 */
@Data
public class SyncAssessResponse {
    private String name;    //学生名称
    private String bookName;    //课时名称
    private int correctCount;       //正确数量
    private int errorCount;       //错误数量
    private int problemCount;       //问题数量
}
