package pw.cdmi.aws.edu.weixin.service;

import pw.cdmi.aws.edu.weixin.rs.v1.domain.WxUserInfo;

/**
 * 微信开放平台获取数据
 */
public interface WxOauth2Service {
    /**
     * 获取微信用户信息
     */
    public WxUserInfo getWxUserInfo(String code);

    /**
     * 获取用户信息
     * @param code
     * @param iv
     * @param encryptedData
     * @return  微信用户信息
     */
    public WxUserInfo getWxUserInfo(String code, String iv, String encryptedData);

}
