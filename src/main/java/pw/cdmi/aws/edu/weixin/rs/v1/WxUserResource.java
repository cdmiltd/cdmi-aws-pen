package pw.cdmi.aws.edu.weixin.rs.v1;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.console.modules.UserRole;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.rs.response.UserToken;
import pw.cdmi.aws.edu.console.services.AppUserService;
import pw.cdmi.aws.edu.console.services.UserTokenHelper;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;
import pw.cdmi.aws.edu.guarder.service.GuarderService;
import pw.cdmi.aws.edu.weixin.Exception.WxErrorMessages;
import pw.cdmi.aws.edu.weixin.rs.v1.domain.BindPhoneRequest;
import pw.cdmi.aws.edu.weixin.rs.v1.domain.RestWxUserLoginRequest;
import pw.cdmi.aws.edu.weixin.rs.v1.domain.WxUserInfo;
import pw.cdmi.aws.edu.weixin.service.WxOauth2Service;
import pw.cdmi.core.exception.HttpClientException;
import pw.cdmi.core.exception.HttpServiceException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wx/v1")
public class WxUserResource {

    private static final Logger logger = LoggerFactory.getLogger(WxUserResource.class);

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private WxOauth2Service wxOauth2Service;

    @Autowired
    private GuarderService guarderService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserTokenHelper userTokenHelper;

    //验证码缓存前缀
    private String CAPTCHA_PREFIX = "captcha/";

    /**
     * 微信小程序登录系统
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public UserToken login(@RequestBody RestWxUserLoginRequest loginRequest, HttpServletRequest request) {
        WxUserInfo wxUserInfo = getUserInfo(loginRequest);
        AppUserEntity appUser = appUserService.getAppUserEntityByWxOpenId(wxUserInfo.getOpenId());
        if(appUser == null){
            logger.info("家长账号不存在，创建账号。openId：{}，nickName:{}", wxUserInfo.getOpenId(), wxUserInfo.getNickName());

            appUser = new AppUserEntity();
            appUser.setRoles("[\"" + UserRole.Guarder.name() + "\"]");
            appUser.setWxOpenId(wxUserInfo.getOpenId());
            appUser.setNickName(wxUserInfo.getNickName());
            appUser.setTrueName(wxUserInfo.getNickName());
            appUser.setAvatarUrl(wxUserInfo.getAvatarUrl());
            appUserService.createAppUserEntity(appUser);
        }
        UserToken userToken = userTokenHelper.getUserToken(appUser);
        return userToken;
    }

    /**
     * 验证手机验证码, 绑定微信账号
     */
    @RequestMapping(value = "bindPhone", method = RequestMethod.POST)
    @ResponseBody
    public Boolean checkPhoneCaptcha(@RequestHeader(value = "Authorization", defaultValue = "") String token, @RequestBody BindPhoneRequest bindPhoneRequest) {
        bindPhoneRequest.checkParameter();
        //校验手机验证码
        String cacheCaptcha = (String) redisUtil.get(CAPTCHA_PREFIX + bindPhoneRequest.getPhone());
        if(!bindPhoneRequest.getCaptcha().equals(cacheCaptcha)){
            logger.warn("user captcha error, send cacheCaptcha:{}, input captcha:{}", cacheCaptcha, bindPhoneRequest.getCaptcha());
            redisUtil.del(CAPTCHA_PREFIX + bindPhoneRequest.getPhone());
            throw new HttpClientException(WxErrorMessages.CaptchaInvalidException);
        }
        //校验是否为家长手机号
        List<GuarderEntity> geList = guarderService.findByPhone(bindPhoneRequest.getPhone());
        if(geList.isEmpty()){
            throw new HttpClientException(WxErrorMessages.GuarderPhoneInvalidException);
        }
        //校验token是否无效
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        if(userToken == null){
            throw new HttpClientException(ErrorMessages.TokenInValidException);
        }
        //查询登录账号，微信号和家长手机号绑定
        AppUserEntity appUserEntity = appUserService.getOne(userToken.getUserId());
        //查询该手机号是否已存在账号表中
        AppUserEntity aue = appUserService.getAppUserEntityByPhone(bindPhoneRequest.getPhone());
        if(aue != null){
            if(!aue.getRoles().contains(UserRole.Guarder.name())){
                List<String> roles = aue.getRoles();
                roles.add(UserRole.Guarder.name());
                aue.setRoles(JSONArray.toJSONString(roles));
            }
            aue.setAvatarUrl(appUserEntity.getAvatarUrl());
            aue.setWxOpenId(appUserEntity.getWxOpenId());
            appUserService.updateAppUserEntity(aue);
            appUserService.deleteAppUserEntityById(appUserEntity.getId());
            //更新登录账号缓存
            userToken.setPhone(bindPhoneRequest.getPhone());
            userToken.setName(aue.getTrueName());
        }else {
            appUserEntity.setPhone(bindPhoneRequest.getPhone());
            appUserService.updateAppUserEntity(appUserEntity);
            //更新登录账号缓存
            userToken.setPhone(bindPhoneRequest.getPhone());
        }
        redisUtil.set(token, userToken, 1800);
        return Boolean.TRUE;
    }

    public WxUserInfo getUserInfo(RestWxUserLoginRequest loginRequest){
        loginRequest.checkParameter();

        WxUserInfo wxUserInfo = wxOauth2Service.getWxUserInfo(loginRequest.getCode(), loginRequest.getIv(), loginRequest.getEncryptedData());
        if (wxUserInfo == null) {
            logger.error("WxUser login Failed: wxUserInfo is null.");
            throw new HttpClientException(WxErrorMessages.ParseWxInfoException);
        }
        if (wxUserInfo.hasError()) {
            logger.error("Can't get UserInfo of code {}: errcode={}, errmsg={}", loginRequest.getCode(), wxUserInfo.getErrcode(), wxUserInfo.getErrmsg());
            throw new HttpServiceException(WxErrorMessages.ParseWxInfoException);
        }

        return wxUserInfo;
    }
}
