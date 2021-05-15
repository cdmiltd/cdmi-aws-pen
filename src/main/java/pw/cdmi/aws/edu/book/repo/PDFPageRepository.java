package pw.cdmi.aws.edu.book.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;
import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;

@Repository
public interface PDFPageRepository extends JpaRepository<PDFPageEntity, String>{


	
	public PDFPageEntity getByPageId(Long pageId);
	
	@Query(value = "delete from edu_pdf_pages where source_id= :sourceId and type = :type")
	@Modifying
	@Transactional
	public void deleteSoucePageId(String sourceId,PDFTypeEnum type);
}
