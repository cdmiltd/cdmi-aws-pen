package pw.cdmi.aws.edu.guarder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.guarder.modules.entities.GuarderInfo;
import pw.cdmi.aws.edu.guarder.repo.GuarderInfoRepository;

@Service
public class InnerJoinService {

	
	@Autowired
	private GuarderInfoRepository gir;
	
	public List<GuarderInfo> selectGuarderInfo(List<String> ids){
		return gir.selectGuarderInfo(ids);
	}
}
