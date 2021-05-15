package pw.cdmi.aws.edu.pen.modules;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PenPoint {

	public static final String redis_prefix = "PEN_SYNC_%s"; // 存入redis key

	private Integer paper_type;
	private Long page_id;
	private Integer x;
	private Integer y;
	private Integer press;
	private Boolean stroke_start;
	private Boolean is_last;
	private Integer time_stamp;
	private Float page_width; // mm
	private Float page_height; // mm

	private Float converX;
	
	private Float converY;
	

	public Float getConverX() {
		return this.x * 0.021167F;
	}

	public Float getConverY() {
		return this.y * 0.021167F;
	}
}
