package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;

import java.util.Date;

/**
 * 教师端-班级课时编号、知识点名称、未审批数量
 */
@Data
public class ClassteamClasshourResponse {
    private String id;  //课时编号
    private String classhourName;   //课时名称
    private String knowledgeName;   //知识点名称
    private Integer number;     //未批改人数
    private Date date;
}
