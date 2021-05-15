package pw.cdmi.aws.edu.assess.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.assess.modules.entities.ClassHourAssess;
import pw.cdmi.aws.edu.assess.modules.entities.KnowledgeAssess;
import pw.cdmi.aws.edu.assess.rs.responses.ClassStudentAssessResponse;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;

import java.util.List;

@Repository
public interface ClassHourAssessRepository extends JpaRepository<ClassHourAssess, String> {
	
	public ClassHourAssess getByClasshourIdAndStudentId(String classhourId,String studentId);

	List<ClassHourAssess> findByClassTeamIdOrderByAssessDateDesc(String classteamId);

	Integer countByClasshourIdAndClassTeamId(String classhourId, String classteamId);

	List<ClassHourAssess> findByClasshourIdAndClassTeamIdOrderByAssessDateDesc(String classhourId, String classteamId);

//	@Query("select avg(errorCount) from edu_classhour_assess where classTeamId=?1 group by knowledgeId")
//	List avgByClassteamIdGroupByKnowledgeId(String classteamId);
}
