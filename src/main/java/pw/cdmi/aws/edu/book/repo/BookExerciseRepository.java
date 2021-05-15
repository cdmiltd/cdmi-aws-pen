package pw.cdmi.aws.edu.book.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.book.modules.entities.BookExerciseEntity;

@Repository
public interface BookExerciseRepository extends JpaRepository<BookExerciseEntity, String> {
}
