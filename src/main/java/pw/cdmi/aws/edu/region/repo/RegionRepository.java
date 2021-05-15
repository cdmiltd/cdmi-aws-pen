package pw.cdmi.aws.edu.region.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.region.modules.entites.RegionEntity;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, String>{

}
