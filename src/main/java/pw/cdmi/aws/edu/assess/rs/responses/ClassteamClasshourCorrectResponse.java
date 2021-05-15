package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;

/**
 * 教师端-班级课时批改统计
 */
@Data
public class ClassteamClasshourCorrectResponse {
    private String classhourId;     //课时名称
    private String classhourName;   //课时名称
    private String knowledgeName;   //知识点名称
    private String correctNumber;   //批改数量
}
