package pw.cdmi.aws.edu.guarder.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.guarder.modules.entities.GuarderInfo;

@Repository
public interface GuarderInfoRepository extends JpaRepository<GuarderInfo, String>{
	
	@Query(value="select g.id,g.name,g.phone,gs.relation,gs.studentId from edu_guarder g inner join edu_guarder_student gs on(g.id = gs.guarderId) where gs.studentId in :ids",nativeQuery = true)
	public List<GuarderInfo> selectGuarderInfo(List<String> ids);
}
