package pw.cdmi.aws.edu.school.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;

import java.util.List;

@Repository
public interface SchoolClassTeamRepository extends JpaRepository<SchoolClassTeamEntity, String>{
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update edu_school_classteam set student_num = student_num + 1 where id = :classteamid",nativeQuery = true)
	int updateStudentNum(String classteamid);
	List<SchoolClassTeamEntity> findBySchoolIdAndStageAndEndYear(String schoolId, StudyStage stage, Integer endYear);
}
