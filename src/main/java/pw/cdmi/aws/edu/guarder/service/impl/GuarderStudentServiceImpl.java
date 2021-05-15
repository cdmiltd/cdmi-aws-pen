package pw.cdmi.aws.edu.guarder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;
import pw.cdmi.aws.edu.guarder.repo.GuarderStudentRepository;
import pw.cdmi.aws.edu.guarder.service.GuarderStudentService;

@Service
public class GuarderStudentServiceImpl extends BaseServiceImpl<GuarderStudentEntity, String> implements GuarderStudentService{

	@Autowired
	GuarderStudentRepository repo;
}
