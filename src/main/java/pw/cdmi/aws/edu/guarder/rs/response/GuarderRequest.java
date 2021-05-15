package pw.cdmi.aws.edu.guarder.rs.response;

import lombok.Data;

@Data
public class GuarderRequest {
    private String name;
    private String phone;
    private String relation;
}
