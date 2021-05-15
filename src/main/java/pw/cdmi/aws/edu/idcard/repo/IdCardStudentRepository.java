package pw.cdmi.aws.edu.idcard.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;

@Repository
public interface IdCardStudentRepository extends JpaRepository<IdCardStudentEntity, String> {

	
	
	@Modifying
	@Transactional
	@Query(value="delete from edu_id_card_student where cardId in :ids")
	public int deleteByCardIds(List<String> ids);
	
	
	@Query(value="select cs from edu_id_card_student cs where cs.cardId = :cardId and cs.upleftX < :x and cs.lowRightX > :x and cs.upleftY < :y and cs.lowRightY > :y ")
	public IdCardStudentEntity selectStudentByRange(String cardId,float x,float y);
}
