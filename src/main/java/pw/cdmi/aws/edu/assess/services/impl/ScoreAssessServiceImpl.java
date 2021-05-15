package pw.cdmi.aws.edu.assess.services.impl;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.cdmi.aws.edu.assess.modules.entities.ScoreAssess;
import pw.cdmi.aws.edu.assess.repo.KnowledgeAssessRepository;
import pw.cdmi.aws.edu.assess.repo.ScoreAssessRepository;
import pw.cdmi.aws.edu.assess.services.ScoreAssessService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Service
public class ScoreAssessServiceImpl extends BaseServiceImpl<ScoreAssess, String> implements ScoreAssessService {

    @PersistenceContext
    EntityManager em;

    public List<Map<String,Object>> queryBySql(String sql){
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> list = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    @Autowired
    private ScoreAssessRepository repo;

    @Override
    public ScoreAssess getByTextBookIdAndStudentId(String textBookId, String studentId) {
         return repo.getByTextBookIdAndStudentId(textBookId, studentId);
    }

    @Override
    public List<Map<String, Object>> findTestByClassteamId(String classteamId) {
        String sql = "SELECT eb.name ,eb.Id FROM edu_score_assess esa, edu_book eb where esa.textBookId = eb.Id and classTeamId ='" + classteamId + "' GROUP BY textBookId ORDER BY assessDate DESC;";
        List<Map<String, Object>> data = queryBySql(sql);
        return data;
    }

    @Override
    public List<Map<String, Object>> staticByClassteamIdAndTextBookId(String classteamId, String textBookId) {
        String sql = "SELECT MAX(score) max, MIN(score) min, ROUND(AVG(score), 2) avg FROM edu_score_assess esa where classTeamId = '" + classteamId + "' AND textBookId = '" + textBookId + "';";
        List<Map<String, Object>> data = queryBySql(sql);
        return data;
    }

    @Override
    public List<Map<String, Object>> rankByClassteamIdAndTextBookId(String classteamId, String textBookId) {
        String sql = "SELECT es.name, esa.score FROM edu_score_assess esa, edu_student es where esa.studentId =es.id and classTeamId = '" + classteamId + "' AND textBookId = '" + textBookId + "' ORDER BY score DESC;";
        List<Map<String, Object>> data = queryBySql(sql);
        return data;
    }
}
