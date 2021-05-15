package pw.cdmi.aws.edu.weixin.rs.v1.domain;

import org.apache.commons.lang3.StringUtils;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.core.exception.HttpClientException;

public class RestWxUserLoginRequest {
    private String code;

    private String rawData;

    private String encryptedData;

    private String iv;

    public void checkParameter() {
        if (StringUtils.isBlank (code)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter, "code不能为空");
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

}
