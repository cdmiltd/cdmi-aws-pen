package pw.cdmi.aws.edu.assess.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.assess.modules.entities.KnowledgeAssess;
import pw.cdmi.aws.edu.assess.repo.KnowledgeAssessRepository;
import pw.cdmi.aws.edu.assess.services.AssessKnowService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;

@Service
public class AssessKnowServiceImpl extends BaseServiceImpl<KnowledgeAssess, String> implements AssessKnowService{

    @Autowired
    private KnowledgeAssessRepository knowledgeAssessRepository;


}
