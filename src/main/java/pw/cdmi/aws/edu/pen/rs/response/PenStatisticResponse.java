package pw.cdmi.aws.edu.pen.rs.response;

import lombok.Data;

@Data
public class PenStatisticResponse {
    private Long total;              //笔的总量
    private Long activatCount;          //笔的激活总数量
}
