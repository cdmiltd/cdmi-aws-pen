package pw.cdmi.aws.edu.school.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.repo.SchoolClassTeamRepository;
import pw.cdmi.aws.edu.school.rs.requests.ClassTeamRequest;
import pw.cdmi.aws.edu.school.services.ClassTeamService;
import pw.cdmi.core.exception.HttpClientException;

@Service
public class ClassTeamServiceImpl extends BaseServiceImpl<SchoolClassTeamEntity, String> implements ClassTeamService {
   
	@Autowired
    private SchoolClassTeamRepository repo;
	

    @Override
    public void initClassTeam(String schoolid, GlobalGradeStage grade, int count) {
    	Date date = new Date();
        for(int i=1;i<=count;i++) {
            SchoolClassTeamEntity team = new SchoolClassTeamEntity();
            team.setSchoolId(schoolid);
            team.setOrderValue(i);
            team.setCreatedDate(date);
            team.setStudentNum(0);
            team.setStage(grade.getStudyStage()); 
            team.setEndYear(DateUtil.getEndYear(grade));   //毕业年份
            repo.save(team);
        }
       
    }

    @Override
	public void createClassTeam(String schoolid, ClassTeamRequest req) {
    	
    	SchoolClassTeamEntity e = new SchoolClassTeamEntity();
    	e.setOrderValue(req.getOrderValue());
    	e.setStage(req.getGrade().getStudyStage());
    	e.setEndYear(DateUtil.getEndYear(req.getGrade()));
    	e.setSchoolId(schoolid);
    	Optional<SchoolClassTeamEntity> opt = repo.findOne(Example.of(e));
    	if(opt.isPresent()) {
    		throw new HttpClientException(ErrorMessages.ExistsDataException);
    	}
    	SchoolClassTeamEntity entity = new SchoolClassTeamEntity();
    	entity.setOrderValue(req.getOrderValue());
    	entity.setSchoolId(schoolid);
    	entity.setCreatedDate(new Date());
    	entity.setStudentNum(0);
    	entity.setAdviser(req.getAdviserId());
    	entity.setName(req.getName());
    	entity.setStage(req.getGrade().getStudyStage());
    	entity.setEndYear(DateUtil.getEndYear(req.getGrade()));
    	repo.save(entity);
    	
    	
	}

    @Override
    public List<SchoolClassTeamEntity> findBySchoolIdAndStageAndEndYear(String schoolId, StudyStage stage, Integer endYear) {
        return repo.findBySchoolIdAndStageAndEndYear(schoolId, stage, endYear);
    }


    @Override
    public void setClassTeamAdviser(String teamId, String teacherId) {
        SchoolClassTeamEntity team  = repo.getOne(teamId);
        team.setAdviser(teacherId);
        repo.saveAndFlush(team);
    }

    @Override
    public void editClassTeam(String id, String name, String teacherId) {
        SchoolClassTeamEntity old_team  = repo.getOne(id);
        old_team.setName(name);
        old_team.setAdviser(teacherId);
        repo.saveAndFlush(old_team);
    }

    @Override
    public void delectClassTeam(String id) {
        //TODO 班级下有学生不能删除
        //TODO 班级下有老师不能删除
        repo.deleteById(id);
    }

    @Override
    public void batchDelectClassTeam(String[] ids) {
        //TODO 班级下有学生不能删除
        //TODO 班级下有老师不能删除
        for(String id: ids) {
            repo.deleteById(id);
        }
    }


  

	
}
