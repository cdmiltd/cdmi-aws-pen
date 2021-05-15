package pw.cdmi.aws.edu.book.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;

@Repository
public interface BookClassHourRepository extends JpaRepository<BookClassHourEntity, String> {
	
	@Query(value = "select max(orderValue) from edu_book_classhour where bookId = :bookId")
	public Integer selectMaxOrderVal(String bookId);
	
	@Query(value = "select c from edu_book_classhour c where c.beginPageNo >=:pageNo and c.endPageNo <=:pageNo ")
	public BookClassHourEntity selectBookClassHourByPageNo(Long pageNo);
}
