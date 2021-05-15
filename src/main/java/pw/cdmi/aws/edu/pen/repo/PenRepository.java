package pw.cdmi.aws.edu.pen.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.pen.modules.entities.PenEntities;

@Repository
public interface PenRepository extends JpaRepository<PenEntities, String> {

}
