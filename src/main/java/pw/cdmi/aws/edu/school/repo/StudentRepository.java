package pw.cdmi.aws.edu.school.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String>{

	
	public List<StudentEntity> findAllByClassteamid(String classteamid);
	
	
	@Query(value = "select max(cardSn) from edu_student")
	public Long selectMaxCardSn();
	
	public StudentEntity findByClassteamidAndSn(String classteamid,Integer sn);
}
