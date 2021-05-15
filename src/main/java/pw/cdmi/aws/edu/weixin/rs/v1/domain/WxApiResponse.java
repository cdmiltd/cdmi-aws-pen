package pw.cdmi.aws.edu.weixin.rs.v1.domain;

import java.io.Serializable;

/************************************************************
 * @author Rox
 * @version 3.0.1
 * @Description: <pre>微信API消息响应类</pre>
 * @Project
 ************************************************************/
public class WxApiResponse implements Serializable {

    Integer errcode;
    String errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean hasError() {
        return errcode != null && errcode != 0;
    }
}
