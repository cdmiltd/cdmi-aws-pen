package pw.cdmi.aws.edu.pen.rs.request;

import lombok.Data;
import lombok.ToString;
import pw.cdmi.aws.edu.pen.modules.PenPoint;

@Data
@ToString
public class PenTrackRequest {

	private String mac;
	
	private PenPoint[] points;


}
