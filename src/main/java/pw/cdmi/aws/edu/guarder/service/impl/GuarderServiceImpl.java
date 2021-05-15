package pw.cdmi.aws.edu.guarder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;
import pw.cdmi.aws.edu.guarder.repo.GuarderRepository;
import pw.cdmi.aws.edu.guarder.rs.response.GuarderRequest;
import pw.cdmi.aws.edu.guarder.service.GuarderService;
import pw.cdmi.aws.edu.guarder.service.GuarderStudentService;

import java.util.List;

@Service
public class GuarderServiceImpl extends BaseServiceImpl<GuarderEntity, String> implements GuarderService{

	
	@Autowired
	private GuarderRepository repo;
	
	@Autowired
	private GuarderStudentService gss;
	
	@Override
	public GuarderEntity createGuarder(String studentid, GuarderRequest req) {

		GuarderEntity e = new GuarderEntity();
		e.setPhone(req.getPhone());
		GuarderEntity ge = findOne(Example.of(e));
		
		if(ge == null) {
			ge = new GuarderEntity();
			ge.setName(req.getName());
			ge.setPhone(req.getPhone());
			repo.save(ge);
		}
		
		GuarderStudentEntity gse = new GuarderStudentEntity();
		gse.setGuarderId(ge.getId());
		gse.setRelation(req.getRelation());
		gse.setStudentId(studentid);
		
		gss.save(gse);
		
		return ge;
		
	}

	@Override
	public void modifyGuarder(GuarderStudentEntity gse,GuarderEntity ge,GuarderRequest req) {
	
		ge.setName(req.getName());
		ge.setPhone(req.getPhone());
		repo.saveAndFlush(ge);
		
		
		gse.setRelation(req.getRelation());
		gss.saveAndFlush(gse);
		
	}

	@Override
	public List<GuarderEntity> findByPhone(String phone) {
		return repo.findByPhone(phone);
	}

}
