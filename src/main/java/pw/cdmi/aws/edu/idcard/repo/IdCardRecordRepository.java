package pw.cdmi.aws.edu.idcard.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.idcard.entity.IdCardRecordEntity;

@Repository
public interface IdCardRecordRepository extends JpaRepository<IdCardRecordEntity, String> {

	
	@Query(value = "select r from edu_id_card_record r where r.startPageId <=?1 and r.endPageId >= ?1")
	public IdCardRecordEntity selectByPageId(Long pageid);
}
