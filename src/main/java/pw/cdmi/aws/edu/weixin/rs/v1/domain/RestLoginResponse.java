package pw.cdmi.aws.edu.weixin.rs.v1.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class RestLoginResponse {
    private String id;
    private String userId;
    private String wxUserId;
    private String headImageUrl;
    private String name;
    private Double balance = 0.0;
    private Double orgBalance = 0.0;
    private String phone;
    private JSONObject parentLanguage;
    private JSONArray studyLanguage;
    private Date expireTime;    //xlingual课程过期时间
    private Boolean isBuy;      //是否购买
    private Boolean isOrgAdmin = false;     //是否为企业管理员
    private Integer orgId;       //企业编号
    private String orgNick;     //企业商户昵称
    private String orgHeadImage;    //企业头像
    private String token;

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public Boolean getBuy() {
        return isBuy;
    }

    public void setBuy(Boolean buy) {
        isBuy = buy;
    }

    public Double getOrgBalance() {
        return orgBalance;
    }

    public void setOrgBalance(Double orgBalance) {
        this.orgBalance = orgBalance;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public Boolean getOrgAdmin() {
        return isOrgAdmin;
    }

    public void setOrgAdmin(Boolean orgAdmin) {
        isOrgAdmin = orgAdmin;
    }

    public String getOrgHeadImage() {
        return orgHeadImage;
    }

    public void setOrgHeadImage(String orgHeadImage) {
        this.orgHeadImage = orgHeadImage;
    }

    public String getOrgNick() {
        return orgNick;
    }

    public void setOrgNick(String orgNick) {
        this.orgNick = orgNick;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public JSONObject getParentLanguage() {
        return parentLanguage;
    }

    public void setParentLanguage(JSONObject parentLanguage) {
        this.parentLanguage = parentLanguage;
    }

    public JSONArray getStudyLanguage() {
        return studyLanguage;
    }

    public void setStudyLanguage(JSONArray studyLanguage) {
        this.studyLanguage = studyLanguage;
    }
}
