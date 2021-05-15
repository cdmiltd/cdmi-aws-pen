package pw.cdmi.aws.edu.console.services;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.utils.RandomKeyUUID;
import pw.cdmi.aws.edu.console.Exception.ConsoleErrorMessages;
import pw.cdmi.aws.edu.console.modules.UserRole;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.rs.response.UserToken;
import pw.cdmi.core.exception.HttpServiceException;

@Service
public class UserTokenHelper {

    private final static Logger logger = LoggerFactory.getLogger(UserTokenHelper.class);

    public final String APPID_SEP = "/";

    @Autowired
    private RedisUtil redisUtil;

    public String generateToken() {
        String appId = "edu";
        return new StringBuilder().append(appId)
                .append(APPID_SEP)
                .append(RandomKeyUUID.getSecureRandomUUID())
                .toString();
    }

    public UserToken getUserToken(AppUserEntity appUserEntity){
        UserToken userToken = new UserToken();
        String token = generateToken();
        userToken.setToken(token);
        userToken.setUserId(appUserEntity.getId());
        userToken.setName(appUserEntity.getNickName());
        userToken.setRole(appUserEntity.getRolesStr());
        userToken.setRoles(appUserEntity.getRoles());
        userToken.setPhone(appUserEntity.getPhone());
        userToken.setAvatarUrl(appUserEntity.getAvatarUrl());
        if(!appUserEntity.getRoles().isEmpty()){
           String roleStr = appUserEntity.getRoles().get(0);
            userToken.setCurrentRole(UserRole.fromName(roleStr));
        }
        redisUtil.set(token, userToken, 1800);
        return userToken;
    }

    public UserToken checkTokenAndGetUserToken(String tokenKey) {
        tokenKey = tokenKey.trim();
        UserToken userToken = (UserToken) redisUtil.get(tokenKey);
        if (userToken == null) {
            throw new HttpServiceException(ConsoleErrorMessages.TokenInvalidException);
        }
        if (userToken != null) {
            long expire = redisUtil.getExpire(tokenKey);
            if (expire < 1000) {
                //刷新token过期时间
                redisUtil.expire(tokenKey, 1800);
            }
        }
        return userToken;
    }

    /**
     * 删除token
     *
     * @param token
     */
    public void deleteUserToken(String token) {
        redisUtil.del(token);
    }
}
