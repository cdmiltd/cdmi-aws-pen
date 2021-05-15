package pw.cdmi.aws.edu.assess.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pw.cdmi.aws.edu.assess.modules.entities.KnowledgeAssess;

@Repository
public interface KnowledgeAssessRepository extends JpaRepository<KnowledgeAssess, String>{
	
	
	public KnowledgeAssess getByKnowledgeIdAndStudentId(String knowledgeId,String studentId);
}
