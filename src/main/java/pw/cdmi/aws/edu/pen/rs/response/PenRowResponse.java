package pw.cdmi.aws.edu.pen.rs.response;

import lombok.Data;

@Data
public class PenRowResponse {
    private String id;   //信息编号
    private String mac;  //Mac地址
    private boolean activated;  //是否已激活
}
