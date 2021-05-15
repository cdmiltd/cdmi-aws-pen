package pw.cdmi.aws.edu.book.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;

@Repository
public interface BookKnowledgeRepository extends JpaRepository<BookKnowledgeEntity, String> {
	
	@Query(value = "select max(sortOrderValue) from edu_book_knowledge where bookId = :bookId")
	public Integer selectMaxOrderVal(String bookId);
}
