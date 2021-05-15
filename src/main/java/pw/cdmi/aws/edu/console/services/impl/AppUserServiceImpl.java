package pw.cdmi.aws.edu.console.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.repo.AppUserRepository;
import pw.cdmi.aws.edu.console.services.AppUserService;

import java.util.Date;

@Service
public class AppUserServiceImpl extends BaseServiceImpl<AppUserEntity, String> implements AppUserService{

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public AppUserEntity createAppUserEntity(AppUserEntity appUserEntity) {
        appUserEntity.setCreateTime(new Date());
        return appUserRepository.save(appUserEntity);
    }

    @Override
    public AppUserEntity updateAppUserEntity(AppUserEntity appUserEntity) {
        appUserEntity.setUpdateTime(new Date());
        return appUserRepository.save(appUserEntity);
    }

    @Override
    public void deleteAppUserEntityById(String id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public AppUserEntity getAppUserEntityById(String id) {
        return appUserRepository.getOne(id);
    }

    @Override
    public AppUserEntity getAppUserEntityByPhone(String phone) {
        return appUserRepository.getByPhone(phone);
    }

    @Override
    public AppUserEntity getAppUserEntityByWxOpenId(String wxOpenId) {
        return appUserRepository.getByWxOpenId(wxOpenId);
    }

    @Override
    public AppUserEntity getAppUserEntityByPhoneAndPasswd(String phone, String passwd) {
        return appUserRepository.getByPhoneAndPassword(phone, passwd);
    }

    @Override
    public Page<AppUserEntity> findAppUserEntityForPage(Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return appUserRepository.findAll(pageable);
    }
}
