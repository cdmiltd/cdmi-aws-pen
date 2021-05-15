package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;

@Data
public class BookClasshourAssessResponse {
    private String studentId;   //学生编号
    private String studentName;     //学生名称
    private String orderValue;      //课时
    private int correctCount;       //正确数量
    private int errorCount;       //错误数量
    private int problemCount;       //问题数量
}
