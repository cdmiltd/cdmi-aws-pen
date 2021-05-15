package pw.cdmi.aws.edu.assess.services.impl;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.cdmi.aws.edu.assess.modules.entities.ClassHourAssess;
import pw.cdmi.aws.edu.assess.repo.ClassHourAssessRepository;
import pw.cdmi.aws.edu.assess.rs.responses.ClassStudentAssessResponse;
import pw.cdmi.aws.edu.assess.rs.responses.StudentClassHourResponse;
import pw.cdmi.aws.edu.assess.rs.responses.StudentKnowResponse;
import pw.cdmi.aws.edu.assess.rs.responses.SyncAssessResponse;
import pw.cdmi.aws.edu.assess.services.AssessService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AssessServiceImpl extends BaseServiceImpl<ClassHourAssess, String> implements AssessService {

    @Autowired
    private ClassHourAssessRepository classHourAssessRepository;

    @PersistenceContext
    EntityManager em;

    public List<Map<String,Object>> queryBySql(String sql){
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> list = query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    @Override
    public List<ClassStudentAssessResponse> findClassStudentAssess(String schoolId, String classteamId) {
        String sql = "SELECT es.id, es.name , cj.correctCount, cj.errorCount, cj.problemCount FROM edu_student es left join\n" +
                "(SELECT eics.studentId, edl.correctCount, edl.errorCount, edl.problemCount FROM edu_draw_line edl, edu_id_card_student eics where edl.idCardStudentId = eics.id) cj on es.id = cj.studentId \n" +
                "where es.schoolId = '" + schoolId +"' and class_team_id = '" + classteamId + "';";
        List<ClassStudentAssessResponse> list = new ArrayList<>();
        List<Map<String, Object>> data = queryBySql(sql);
        if(!data.isEmpty()){
            for (Map<String, Object> raw: data){
                ClassStudentAssessResponse cs = new ClassStudentAssessResponse();
                cs.setStudentId((String) raw.get("id"));
                cs.setStudentName((String) raw.get("name"));
                cs.setCorrectCount(raw.get("correctCount")==null?0:(int)(raw.get("correctCount")));
                cs.setErrorCount(raw.get("errorCount")==null?0:(int)raw.get("errorCount"));
                cs.setProblemCount(raw.get("problemCount")==null?0:(int)raw.get("problemCount"));
                list.add(cs);
            }
        }
        return list;
    }

    @Override
    public List<SyncAssessResponse> findSyncAssess(String mac) {
        String sql = "SELECT es.`name`, eb.`name` bookname, edl.pageNo, edl.correctCount, edl.errorCount, edl.problemCount FROM edu_draw_line edl, edu_student es, edu_book eb WHERE edl.studentId = es.id and edl.pageNo BETWEEN eb.start_pageno AND eb.end_pageno AND edl.mac='" + mac + "' ORDER BY statisTime DESC";
        List<SyncAssessResponse> list = new ArrayList<>();
        List<Map<String, Object>> data = queryBySql(sql);
        if(!data.isEmpty()){
            for (Map<String, Object> raw: data){
                SyncAssessResponse cs = new SyncAssessResponse();
                cs.setName((String) raw.get("name"));
                cs.setBookName((String) raw.get("bookname"));
                cs.setCorrectCount(raw.get("correctCount")==null?0:(int)(raw.get("correctCount")));
                cs.setErrorCount(raw.get("errorCount")==null?0:(int)raw.get("errorCount"));
                cs.setProblemCount(raw.get("problemCount")==null?0:(int)raw.get("problemCount"));
                list.add(cs);
            }
        }
        return list;
    }

    @Override
    public List<StudentClassHourResponse> assessClassHourByUserIdAndCourse(String userId, String course) {
        String sql = "SELECT eb.name, eb.course, ebc.id , ebk.title, ebc.orderValue, edl.correctCount , edl.errorCount ,edl.problemCount FROM edu_book eb ,edu_book_knowledge ebk, edu_book_classhour ebc, edu_draw_line edl where ebc.knowledges = ebk.Id and ebk.bookId = eb.Id and edl.pageNo BETWEEN ebc.beginPageNo and ebc.endPageNo and edl.studentId = '"+ userId +"' and eb.course = '" + course + "'";
        List<StudentClassHourResponse> list = new ArrayList<>();
        List<Map<String, Object>> data = queryBySql(sql);
        if(!data.isEmpty()){
            for (Map<String, Object> raw: data){
                StudentClassHourResponse cs = new StudentClassHourResponse();
                cs.setClasshourId((String) raw.get("id"));
                cs.setBookName((String) raw.get("name"));
                cs.setKnowName ((String) raw.get("title"));
                cs.setOrderValue((Integer) raw.get("orderValue"));
                cs.setCorrectCount(raw.get("correctCount")==null?0:(int)(raw.get("correctCount")));
                cs.setErrorCount(raw.get("errorCount")==null?0:(int)raw.get("errorCount"));
                cs.setProblemCount(raw.get("problemCount")==null?0:(int)raw.get("problemCount"));
                list.add(cs);
            }
        }
        return list;
    }

    @Override
    public List<StudentKnowResponse> assessKnowByUserIdAndCourse(String userId, String course) {
        String sql = "SELECT eb.name, eb.course, ebk.id ,ebk.title, sum(edl.correctCount) as correctCount, sum(edl.errorCount) as errorCount,sum(edl.problemCount) as problemCount FROM edu_book eb ,edu_book_knowledge ebk, edu_book_classhour ebc, edu_draw_line edl where ebc.knowledges = ebk.Id and ebk.bookId = eb.Id and edl.pageNo BETWEEN ebc.beginPageNo and ebc.endPageNo and edl.studentId = '"+ userId +"' and eb.course = '" + course + "' group by ebk.title";
        List<StudentKnowResponse> list = new ArrayList<>();
        List<Map<String, Object>> data = queryBySql(sql);
        if(!data.isEmpty()){
            for (Map<String, Object> raw: data){
                StudentKnowResponse cs = new StudentKnowResponse();
                cs.setKnowledgeId((String) raw.get("id"));
                cs.setBookName((String) raw.get("name"));
                cs.setKnowName ((String) raw.get("title"));
                cs.setCorrectCount(raw.get("correctCount")==null?0:Integer.valueOf(raw.get("correctCount").toString()));
                cs.setErrorCount(raw.get("errorCount")==null?0:Integer.valueOf(raw.get("errorCount").toString()));
                cs.setProblemCount(raw.get("problemCount")==null?0:Integer.valueOf(raw.get("problemCount").toString()));
                list.add(cs);
            }
        }
        return list;
    }

    @Override
    public Integer assessStuCourseClassHourRank(String classHourId, String classTeamId, Integer errorCount) {
        String sql = "SELECT count(*) count FROM edu_classhour_assess eca where eca.classTeamId = '" + classTeamId + "' AND eca.classhourId = '" + classHourId + "' and eca.errorCount < " + errorCount;
        List<Map<String, Object>> data = queryBySql(sql);
        Integer rank = 1;
        if(!data.isEmpty()){
            rank = (int)Float.parseFloat(String.valueOf(data.get(0).get("count"))) + 1;
        }
        return rank;
    }

    @Override
    public Integer assessStuCourseKnowRank(String knowId, String classTeamId, Integer errorCount) {
        String sql = "SELECT count(*) count FROM edu_knowledge_assess eka where eka.classTeamId = '" + classTeamId + "' AND eka.knowledgeId = '" + knowId + "' and eka.errorCount < " + errorCount;
        List<Map<String, Object>> data = queryBySql(sql);
        Integer rank = 1;
        if(!data.isEmpty()){
            rank = (int)Float.parseFloat(String.valueOf(data.get(0).get("count"))) + 1;
        }
        return rank;
    }


}
