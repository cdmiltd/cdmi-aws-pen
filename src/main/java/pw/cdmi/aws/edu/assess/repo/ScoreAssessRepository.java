package pw.cdmi.aws.edu.assess.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.assess.modules.entities.ScoreAssess;

@Repository
public interface ScoreAssessRepository extends JpaRepository<ScoreAssess, String> {
    ScoreAssess getByTextBookIdAndStudentId(String textBookId, String studentId);
}
