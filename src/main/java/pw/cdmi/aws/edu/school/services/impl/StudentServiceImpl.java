package pw.cdmi.aws.edu.school.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;
import pw.cdmi.aws.edu.guarder.service.GuarderService;
import pw.cdmi.aws.edu.guarder.service.GuarderStudentService;
import pw.cdmi.aws.edu.idcard.service.impl.RedisRandomService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.repo.StudentRepository;
import pw.cdmi.aws.edu.school.rs.requests.ExportStudentEntity;
import pw.cdmi.aws.edu.school.rs.requests.StudentRequest;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

@Service
public class StudentServiceImpl extends BaseServiceImpl<StudentEntity, String> implements StudentService {
	
    @Autowired
    private StudentRepository repo;
    
    @Autowired
    private SchoolClassTeamService classService;
    
    @Autowired
    private GuarderService guService;
    
    @Autowired
    private GuarderStudentService gss;
    
	@Autowired
	RedisRandomService redisRandomService;

	@Override
	@Transactional
	public int exportStudent(String classteamid, List<ExportStudentEntity> data) {
		
		SchoolClassTeamEntity classt = classService.getOne(classteamid);
		if(classt == null) { throw new HttpClientException(ErrorMessages.NotFoundObjectKey);}
		int i = 0;
		for (ExportStudentEntity e : data) {
			
			
			
			StudentEntity student = repo.findByClassteamidAndSn(classteamid,Integer.valueOf(e.getSn()));
			if(student == null) student = new StudentEntity();
			
			student.setBirthday(e.getBirthday());
			student.setClassteamid(classteamid);
			student.setName(e.getName());
			student.setSex(e.getSex());
			student.setSn(Integer.valueOf(e.getSn()));
			student.setSchoolId(classt.getSchoolId());
			repo.saveAndFlush(student);
			//家长信息
			
			modifyGuarder(e.getGuarderPhone(), e.getGuarderName(), e.getRelation(), student.getId());
			modifyGuarder(e.getGuarderPhone2(), e.getGuarderName2(),e.getRelation2(), student.getId());
			i++;
			
		}
		return i;
		
	}
	
	@Override
	public String addStudent(StudentRequest req) {
		StudentEntity entity = new StudentEntity();
		BeanUtils.copyProperties(req, entity);
		SchoolClassTeamEntity classEntity = classService.getOne(req.getClassteamid()); 
		if(classEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		entity.setSchoolId(classEntity.getSchoolId());
		
		save(entity);
		
		modifyGuarder(req.getGuarder().getPhone(), req.getGuarder().getName(), req.getGuarder().getRelation(), entity.getId());
		
		return entity.getId();
	}
	
	
	
	public StudentEntity save(StudentEntity entity) {
		entity.setCardSn(redisRandomService.getNextStudentCardId());
		return repo.save(entity);
	}
	
	
	private void modifyGuarder(String phone,String name,String relation,String studentid) {
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(phone)) {
			GuarderEntity e = new GuarderEntity();
			e.setPhone(phone);
			GuarderEntity old = guService.findOne(Example.of(e));
			if(old == null) {
				GuarderEntity entity = new GuarderEntity();
				entity.setPhone(phone);
				entity.setName(name);
				guService.save(entity);
				
				GuarderStudentEntity gse = new GuarderStudentEntity();
				gse.setGuarderId(entity.getId());
				gse.setStudentId(studentid);
				gse.setRelation(relation);
				gss.save(gse);
				
			}else {
				GuarderStudentEntity gse = new GuarderStudentEntity();
				gse.setGuarderId(old.getId());
				gse.setStudentId(studentid);
				gse.setRelation(relation);
				gss.save(gse);
			}
		}
		
		
	}


	


	
	
	
	
	@Override
	public List<StudentEntity> findAllByClassteamid(String classteamid) {
		StudentEntity ex = new StudentEntity();
		ex.setClassteamid(classteamid);
		Sort sort = Sort.by(Direction.ASC,"sn");
		return repo.findAll(Example.of(ex), sort);//repo.findAllByClassteamid(classteamid);
	}
    
    
    
    
}
