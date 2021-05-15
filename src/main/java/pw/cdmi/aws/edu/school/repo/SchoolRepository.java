package pw.cdmi.aws.edu.school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, String>{

	
	SchoolEntity getByManageId(String manageId);
  
}
