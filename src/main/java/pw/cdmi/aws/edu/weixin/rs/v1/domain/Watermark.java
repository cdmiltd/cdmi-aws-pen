package pw.cdmi.aws.edu.weixin.rs.v1.domain;

/************************************************************
 * @author Rox
 * @version 3.0.1
 * @Description: <pre>微信小程序登录用户信息中的水印信息，用于安全检查</pre>
 * @Project
 ************************************************************/
public class Watermark {

    private Integer timestamp;
    private String appid;

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
