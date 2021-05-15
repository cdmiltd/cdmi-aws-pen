package pw.cdmi.aws.edu.console.Exception;


import pw.cdmi.core.exception.ErrorMessage;

import java.text.MessageFormat;

public enum ConsoleErrorMessages implements ErrorMessage {
	
	RandomTokenFailException(10001, "token生成失败", "token生成失败"),
    TokenInvalidException(10002, "token无效", "token无效"),
    PhoneInvalidException(10003, "phone无效", "phone无效"),
    CaptchaInvalidException(10004, "验证码无效", "验证码无效"),
    PasswdInvalidException(10005, "密码无效", "密码无效");
	
    private long code;
    private String message;
    private String logMessage;

    ConsoleErrorMessages(long code, String message, String logMessage){
        this.code = code;
        this.message = message;
        this.logMessage = logMessage;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getLogContent() {
        return this.logMessage;
    }

    @Override
    public void setParams(Object... params) {
        MessageFormat.format(this.message, params);
    }

    @Override
    public long getCode() {
        return this.code;
    }
}
