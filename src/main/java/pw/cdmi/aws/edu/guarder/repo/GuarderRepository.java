package pw.cdmi.aws.edu.guarder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;

import java.util.List;

@Repository
public interface GuarderRepository extends JpaRepository<GuarderEntity, String>{
    List<GuarderEntity> findByPhone(String phone);
}
