package pw.cdmi.aws.edu.common.component;

import java.io.File;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.obs.services.ObsClient;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;

import pw.cdmi.aws.edu.common.enums.HuaWeiOBSEnum;

/**
 * 华为云 obs 上传组件
 * @author liwenping
 *
 */
@Component
public class HuaWeiCloudOBSComponent {

	 private static final Logger log = LoggerFactory.getLogger(HuaWeiCloudOBSComponent.class);
	
	@Value("${huawei.ak}")
	private String ak;
	
	@Value("${huawei.sk}")
	private String sk;
	
	@Value("${huawei.obs.north.endPoint}")
	private String endPoint;
	
	@Value("${huawei.obs.public.bucketName}")
	private String publicBucket;
	
	@Value("${huawei.obs.private.bucketName}")
	private String privateBucket;
	
	
	private ObsClient obsClient;
	
	
	@PostConstruct
	public void init() {
		log.info("初始化华为云obj上传组件");
		obsClient = new ObsClient(ak, sk, "https://"+ endPoint); 
	}
	
	/**
	 * 上传文件到私有库
	 * @param objectKey
	 * @param ins
	 * @return
	 */
	public PutObjectResult uploadPrivateBucket(String objectKey,InputStream ins) {
		
		return obsClient.putObject(privateBucket, objectKey, ins);
	}
	
	/**
	 * 上传文件到公有库
	 * @param objectKey
	 * @param inputStream
	 * @return
	 */
	public PutObjectResult uploadPublicBucket(String objectKey,InputStream inputStream) {
		return obsClient.putObject(publicBucket, objectKey, inputStream);
	}
	
	public PutObjectResult uploadPublicBucket(String objectKey,File file) {
		return obsClient.putObject(publicBucket, objectKey, file);
	}
	
	/**
	 * 
	 * @param type   1:共有   2:私有
  	 * @param objectKey 
	 * @return
	 */
	public ObsObject getObject(HuaWeiOBSEnum obs,String objectKey) {
		if(obs == HuaWeiOBSEnum.Public) {
			return obsClient.getObject(publicBucket, objectKey);
		}else {
			return obsClient.getObject(privateBucket, objectKey);
		}
		
	}
	
	
	/**
	 * 私有库访问地址
	 * @param objectKey 文件名
	 * @param seconds 过期时间秒
	 * @return
	 */
	public TemporarySignatureResponse createTemporarySignature(String objectKey,long seconds) {
		TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, seconds);
		request.setBucketName(privateBucket); 
		request.setObjectKey(objectKey);  
		TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
		return response;
	}
	
}
