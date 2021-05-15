package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;

/**
 * 家长端-知识点统计
 */
@Data
public class StudentKnowResponse {
    private String knowledgeId; //课时编号
    private String bookName;    //课本名称
    private String knowName;   //知识点名称
    private Integer correctCount;    //正确个数
    private Integer errorCount;      //错误个数
    private Integer problemCount;       //问题个数
}
