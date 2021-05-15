package pw.cdmi.aws.edu.school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.school.modules.entities.ClassTeacherEntity;

@Repository
public interface ClassTeacherRepository extends JpaRepository<ClassTeacherEntity,String>{

}
