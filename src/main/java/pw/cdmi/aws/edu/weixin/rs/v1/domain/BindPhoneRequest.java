package pw.cdmi.aws.edu.weixin.rs.v1.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.core.exception.HttpClientException;

@Data
public class BindPhoneRequest {
    private String phone;
    private String captcha;

    public void checkParameter(){
        if (StringUtils.isBlank (phone)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, "phone不能为空");
        }
        if (StringUtils.isBlank (captcha)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, "captcha不能为空");
        }
    }
}
