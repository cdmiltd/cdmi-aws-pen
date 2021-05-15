package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;

@Data
public class ClasshourStatisticResponse {
    private String id;
    private String title;
    private Integer sortOrderValue;
    private Integer max;
    private Integer min;
    private Float average;
}
