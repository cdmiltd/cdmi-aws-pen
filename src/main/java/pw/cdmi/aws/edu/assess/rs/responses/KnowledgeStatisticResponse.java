package pw.cdmi.aws.edu.assess.rs.responses;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
public class KnowledgeStatisticResponse {
    private String id;
    private String title;
    private Integer max;
    private Integer min;
    private Float avg;
    private Date date;
}
