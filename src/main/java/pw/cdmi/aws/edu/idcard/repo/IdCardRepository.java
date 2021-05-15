package pw.cdmi.aws.edu.idcard.repo;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;

@Repository
public interface IdCardRepository extends JpaRepository<IdCardEntity, String> {

	
	@Query(value = "select max(sn) from edu_id_card")
	public Long selectMaxSn();
	
	
	@Modifying
	@Transactional
	@Query(value="delete from edu_id_card where recordId = :recordId")
	public int deleteByRecoredId(String recordId);
	
	
	@Query(value="select c.id, c.classend endYear ,c.order_value orderValue,c.school_id schoolId,c.idCardSn,c.stage,c.encode_idcard_pdf_path idCardPdfPath,s.name schoolName from edu_school_classteam c inner join edu_school s on (s.id = c.school_id) where if(ifnull(:idCardSn,'') != '' , c.idCardSn = :idCardSn , c.idCardSn > 0)  ORDER BY c.idCardSn desc limit :size",nativeQuery = true)
	public List<Map<String,String>> findNewFive(Long idCardSn,Integer size);
	
	
	
	public IdCardEntity getBySn(Long sn);
	
	public IdCardEntity getByPageId(Long pageId);
	
	public IdCardEntity getByClassTeamId(String classTeamId);
	
	
}
