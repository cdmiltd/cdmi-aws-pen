package pw.cdmi.aws.edu.weixin.Exception;

import pw.cdmi.core.exception.ErrorMessage;

public enum WxErrorMessages implements ErrorMessage {
    CaptchaInvalidException(20001, "验证码校验无效", "验证码校验失败"),
    ParseWxInfoException(20002, "解析微信信息失败","解析微信信息失败"),
    GuarderPhoneInvalidException(20003, "家长电话不存在", "家长电话不存在");

    private long code;
    private String message;
    private String logMessage;

    WxErrorMessages(long code, String message, String logMessage){
        this.code = code;
        this.message = message;
        this.logMessage = logMessage;
    }

    @Override
    public long getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getLogContent() {
        return null;
    }

    @Override
    public void setParams(Object... params) {

    }
}


