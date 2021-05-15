package pw.cdmi.aws.edu.common.rs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s3/v1")
public class ImportFileResource {

	
	@Value("${huawei.obs.north.endPoint}")
	private String endPoint;
	
	@Value("${huawei.obs.public.bucketName}")
	private String publicBucket;
	
	@GetMapping("template/student")
	public String studentTemplate() {
		//https://obs-66c8.obs.cn-north-1.myhwclouds.com:443/export_pen_template.xlsx
		StringBuffer sb = new StringBuffer();
		sb.append("https://").append(publicBucket).append(".").append(endPoint).append("/export_student_template.xlsx");
		
		return sb.toString();
	}
	
	@GetMapping("template/pen")
	public String penTemplate() {
		StringBuffer sb = new StringBuffer();
		sb.append("https://").append(publicBucket).append(".").append(endPoint).append("/export_pen_template.xlsx");
		
		return sb.toString();
	}
}
