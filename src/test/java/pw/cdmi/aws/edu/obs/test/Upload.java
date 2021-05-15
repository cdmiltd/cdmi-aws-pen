package pw.cdmi.aws.edu.obs.test;

import java.io.File;
import java.util.Date;
import java.util.stream.LongStream;

import com.alibaba.fastjson.JSON;
import com.obs.services.ObsClient;

public class Upload {

	public static void main(String[] args) {
		System.out.println(JSON.toJSONString(LongStream.rangeClosed(10, 14).toArray()));
//		ObsClient obsClient = new ObsClient("45XTDU0GZUODJHPIBRD3", "XGD4zFS1pl0GyYGwVPAVjKFaT5xVs5m0v9O2sNYT", "https://obs.cn-north-1.myhwclouds.com");
//		
//		com.obs.services.model.ObsObject obj = obsClient.getObject("obs-66c8", "export_student_template.xlsx");
//		
////		com.obs.services.model.PutObjectResult obj = obsClient.putObject("obs-66c8", "export_student_template.xlsx", new File("/Users/liwenping/SpringToolsWorkspace/cdmi-industry-edu/src/main/resources/static/export_student_template.xlsx"));
//		System.out.println(JSON.toJSONString(obj));
		//{"bucketName":"obs-66c8","etag":"\"b2497a3099c859b171a4b548b9df99d0\"","objectKey":"export_pen_template.xlsx","objectUrl":"https://obs-66c8.obs.cn-north-1.myhwclouds.com:443/export_pen_template.xlsx","requestId":"000001779E6CC50D8054E612CA45D8B7","responseHeaders":{"content-length":"0","date":1613270664000,"etag":"\"b2497a3099c859b171a4b548b9df99d0\"","id-2":"32AAAUgAIAABAAAQAAEAABAAAQAAEAABCS9XFXTwuqHmDyZf RcwBaXeqNtBpixG","request-id":"000001779E6CC50D8054E612CA45D8B7"},"statusCode":200}

	}
}
