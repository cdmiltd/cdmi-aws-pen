package pw.cdmi.aws.edu.school.rs.responses;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;

@Data
public class GradeResponse {
    private String title;           //年级显示名
    private String name;            //年级枚举名
    private Integer classNum;       //班级数量
}
