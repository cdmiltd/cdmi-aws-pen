package pw.cdmi.aws.edu.school.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.repo.SchoolClassTeamRepository;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.StudentService;

@Service
public class SchoolClassTeamServiceImpl extends BaseServiceImpl<SchoolClassTeamEntity, String> implements SchoolClassTeamService {

	@Autowired
	private SchoolClassTeamRepository repo;
	
	@Autowired
	private StudentService studentService;
	
	@Override
	public int updateStudentNum(String classteamid) {
		
		StudentEntity ex = new StudentEntity();
		ex.setClassteamid(classteamid);
		
		long count = studentService.count(Example.of(ex));
		
		SchoolClassTeamEntity cTeamEntity = repo.getOne(classteamid);
		cTeamEntity.setStudentNum(Integer.valueOf(count+""));
		repo.saveAndFlush(cTeamEntity);
		return 1;
	}

}
