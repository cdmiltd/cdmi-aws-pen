package pw.cdmi.aws.edu.school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.school.modules.entities.SchoolStudyStageEntity;

@Repository
public interface SchoolStudyStageRepository extends JpaRepository<SchoolStudyStageEntity, String>{

}
