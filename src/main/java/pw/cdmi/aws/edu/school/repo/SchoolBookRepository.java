package pw.cdmi.aws.edu.school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.school.modules.entities.SchoolBookEntity;

@Repository
public interface SchoolBookRepository extends JpaRepository<SchoolBookEntity, String>{

}
