package pw.cdmi.aws.edu.console.rs.request;

import lombok.Data;

@Data
public class BandingChildRequest {
    private String name;        //名称
    private Integer sn;    //学号
    private String schoolId;    //所在学校
    private String classteamId;      //所在班级
}
