package pw.cdmi.aws.edu.test;


import pw.cdmi.core.exception.ErrorMessage;

import java.text.MessageFormat;

public enum ErrorMessages implements ErrorMessage {
    MissRequiredParameter(10101401001l,"客户端缺失必要的参数","缺少{0}参数"),
    NotFoundObjectKey(10101401002l,"客户端传入的对象ID不存在","{0}在系统中并不存在，可能已被删除"),
    VALID_DATA_RANGE(1010140031,"开始日期在结束日期之后","无效请求，参数[{0}:{1},{2}:{3}]"),
    WrongPhoneNumberFormat(10101401004l,"错误的国内手机号码格式错误","输入电话号码为{0}，此为错误的国内手机号码格式错误"),
	
	FileEmpty(1001l,"文件数据为空","文件数据为空"),
	FileUploadError(1002l,"文件上传失败","文件上传失败");

    private long code;
    private String message;
    private String logMessage;

    ErrorMessages(long code, String message, String logMessage){
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
