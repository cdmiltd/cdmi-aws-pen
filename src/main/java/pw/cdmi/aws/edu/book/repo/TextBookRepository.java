package pw.cdmi.aws.edu.book.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;

@Repository
public interface TextBookRepository extends JpaRepository<TextbookEntity, String>{

	@Query(value = "select b from edu_book b where b.startPageno <=?1 and b.endPageno >= ?1")
	public TextbookEntity selectBookByPageno(Long pageid);
	
	
	  @Query(value = "select t.* from edu_book t where 1=1 " +
	            "and if(ifnull(:semester,'') != '' , t.semester = :semester , 1=1) " +
	            "and if(ifnull(:grade,'') != '' , t.grade = :grade , 1=1) " +
	            "and if(ifnull(:stage,'') != '' , t.stage = :stage , 1=1) " +
	            "and if(ifnull(:course,'') != '' , t.course = :course , 1=1) " +
	            "and t.id in (:ids)" , nativeQuery = true)
	public List<TextbookEntity> selectSchoolBook(List<String> ids,String semester,String grade,String course,String stage);
	@Query(value = "select b from edu_book b where b.grade =?1 group by b.course")
	List<TextbookEntity> findByGradeGroupByCourse(GlobalGradeStage gradeStage);
}
