package pw.cdmi.aws.edu.school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, String>{
	
	
	
    TeacherEntity getByPhone(String phone);

   
}
