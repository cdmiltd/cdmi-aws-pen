package pw.cdmi.aws.edu.config.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.config.modules.ObsConfigEntity;

@Repository
public interface ObsConfigRepository extends JpaRepository<ObsConfigEntity	, String>{

}
