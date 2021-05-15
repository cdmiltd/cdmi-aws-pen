package pw.cdmi.aws.edu.console.services;

import org.springframework.data.domain.Page;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;

public interface AppUserService extends BaseService<AppUserEntity, String>{
    /**
     * 创建用户
     * @param appUserEntity
     * @return
     */
    AppUserEntity createAppUserEntity(AppUserEntity appUserEntity);

    /**
     * 更新用户信息
     * @param appUserEntity
     * @return
     */
    AppUserEntity updateAppUserEntity(AppUserEntity appUserEntity);

    /**
     * 删除用户
     * @param id
     */
    void deleteAppUserEntityById(String id);

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    AppUserEntity getAppUserEntityById(String id);

    /**
     * 根据电话号码查询用户
     * @param phone
     * @return
     */
    AppUserEntity getAppUserEntityByPhone(String phone);

    /**
     * 根据电话号码查询用户
     * @param wxOpenId
     * @return
     */
    AppUserEntity getAppUserEntityByWxOpenId(String wxOpenId);

    /**
     * 根据电话号码和密码查询账号信息
     * @param phone
     * @return
     */
    AppUserEntity getAppUserEntityByPhoneAndPasswd(String phone, String passwd);

    /**
     * 查询所用用户信息
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Page<AppUserEntity> findAppUserEntityForPage(Integer pageNumber, Integer pageSize);
}
