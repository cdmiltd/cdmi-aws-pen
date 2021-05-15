package pw.cdmi.aws.edu.pen.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.pen.modules.entities.DrawLineEntities;

import java.util.List;

@Repository
public interface DrawLineRepository extends JpaRepository<DrawLineEntities, String> {
    DrawLineEntities getByMacAndPageNoAndIdCardStudentId(String mac, Long pageNo, String idCardStudentId);
    DrawLineEntities getByPageNoAndStudentId(Long pageNo, String studentId);
    List<DrawLineEntities> findByState(Boolean state);
    List<DrawLineEntities> findByStudentIdAndState(String studentId, Boolean state);
}
