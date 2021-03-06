package pw.cdmi.aws.edu.weixin.rs.v1.domain;

/************************************************************
 * @Description: 微信用户登录后的session key和openid
 ************************************************************/
public class WxSessionKey extends WxApiResponse {
    private String openid;
    private String sessionKey;
    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
