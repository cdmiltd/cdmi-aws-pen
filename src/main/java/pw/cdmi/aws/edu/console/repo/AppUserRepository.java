package pw.cdmi.aws.edu.console.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, String> {
    AppUserEntity getByPhone(String phone);
    AppUserEntity getByPhoneAndPassword(String phone, String passwd);
    AppUserEntity getByWxOpenId(String openId);
}
