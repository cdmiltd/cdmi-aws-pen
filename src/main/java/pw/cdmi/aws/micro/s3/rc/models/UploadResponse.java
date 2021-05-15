package pw.cdmi.aws.micro.s3.rc.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UploadResponse {

	
	private String originalFilename;
	
	private String objectKey;
	
	private String objectUrl;
}
