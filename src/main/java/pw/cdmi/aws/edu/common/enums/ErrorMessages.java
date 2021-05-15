package pw.cdmi.aws.edu.common.enums;


import pw.cdmi.core.exception.ErrorMessage;

import java.text.MessageFormat;

public enum ErrorMessages implements ErrorMessage {
	
	SystemException(99999999l,"程序异常","程序异常"),
	MethodArgumentTypeMismatchException(9000000l,"参数转换异常","参数转换异常"),
	ArgValidException(9000001l,"请求参数校验失败:","请求参数校验失败"),
    TokenInValidException(9000002l,"token无效","token无效"),
	
	ExistsDataException(9000003l,"已经存在此数据","已经存在此数据"),
	
	PageIdExistsException(9000006l,"PageId已经存在","PageId已经存在"),
	
	RepetitionInitException(9000004l,"重复初始化","重复初始化"),
	
	NotFoundGuarder(9000010l,"未找到监护人信息","未找到监护人信息"),
	
	
	NotUploadEncodedIdCardPDF(9000015l,"未上传铺码后的文件","未上传铺码后的文件"),
	PDFPageSizeException(9000017l,"PDF页数和PageId不匹配","PDF页数和PageId不匹配"),
	
	EncodedIdCardPDFNotEqualsSn(9000016l,"铺码PDF文件不是最新的","铺码PDF文件不是最新的"),
	
	DeleteDataException(9000020l,"删除数据权限不足","删除数据权限不足"),
	
	RoleException(9000011l,"权限不足","权限不足"),
	
	AddTeacherRoleException(9000018l,"此手机号其他学校管理员已经使用","此手机号其他学校管理员已经使用"),
	
	ExistsSchoolManageException(9000017l,"当前手机号已经存在其他学校管理员身份","当前手机号已经存在其他学校管理员身份"),
	
	BindStudentOverflow(9000020l,"绑定学生数量超过最大限制","绑定学生数量超过最大限制"),

    MissRequiredParameter(10101401001l,"客户端缺失必要的参数","缺少{0}参数"),
    NotFoundObjectKey(10101401002l,"客户端传入的对象ID不存在","{0}在系统中并不存在，可能已被删除"),
    VALID_DATA_RANGE(1010140031,"开始日期在结束日期之后","无效请求，参数[{0}:{1},{2}:{3}]"),
    WrongPhoneNumberFormat(10101401004l,"错误的国内手机号码格式错误","输入电话号码为{0}，此为错误的国内手机号码格式错误"),
	
    
    BookNotSetPDFPageInfo(1001000014l,"书籍未设置书籍标引信息","书籍未设置书籍标引信息"),
    
    BookPDFPageNoOverflow(1001000014l,"读取书籍PDF页不在范围内","读取书籍PDF页不在范围"),
    
	FileEmpty(1001l,"文件数据为空","文件数据为空"),
	FileNotFound(1003l,"未找到相应文件","未找到相应文件"),
	FileUploadError(1002l,"文件上传失败","文件上传失败"), 
	ClassTeamNotFoundStudentException(9000030l,"班级中没有找到学生","班级中没有找到学生");

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
