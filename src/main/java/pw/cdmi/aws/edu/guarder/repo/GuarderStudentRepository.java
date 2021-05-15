package pw.cdmi.aws.edu.guarder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;

@Repository
public interface GuarderStudentRepository extends JpaRepository<GuarderStudentEntity,String>{

	
}
