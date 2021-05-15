package pw.cdmi.aws.app.config.rs;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/app/v1")
@Tag(name = "app", description = "应用模块")
public class ApplicationConfigResource {
    private static String S3_PROVIDER_NAME = "app.edu.s3.provider.name";
    private static String S3_ENDPOINT_URL = "app.edu.s3.endpoint.url";
    private static String S3_APPKEY = "app.edu.s3.appkey";
    private static String S3_SECRETKEY = "app.edu.s3.secretkey";

    private static String SMS_PROVIDER_NAME = "app.edu.sms.provider.name";
    private static String SMS_ENDPOINT_URL = "app.edu.sms.endpoint.url";
    private static String SMS_AUTOGRAPH = "app.edu.sms.autograph";
    private static String SMS_PASSWORD = "app.edu.sms.password";

    @GetMapping("/version")
    public String getLatestVersion() {
        return null;
    }

    @PutMapping("/config/s3")
    public void setS3Config(Map<String,String> map) {
        if(StringUtils.isBlank(map.get(S3_PROVIDER_NAME))){

        }
        return;
    }

    @GetMapping("/config/s3")
    public String getS3Config() {
        return null;
    }

    @PutMapping("/config/sms")
    public void setSmsConfig(Map<String,String> map) {
        return;
    }

    @GetMapping("/config/sms")
    public Map<String,String> getSmsConfig() {
        return null;
    }
}
