package pw.cdmi.aws.edu.pen.rs.request;

import lombok.Data;

@Data
public class PenRealSyncRequest {
    private String mac;
    private String name;
    private double quantity;    //电量
}
