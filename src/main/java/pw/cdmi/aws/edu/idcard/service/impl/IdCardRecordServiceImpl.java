package pw.cdmi.aws.edu.idcard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.idcard.entity.IdCardRecordEntity;
import pw.cdmi.aws.edu.idcard.repo.IdCardRecordRepository;
import pw.cdmi.aws.edu.idcard.service.IdCardRecordService;

@Service
public class IdCardRecordServiceImpl extends BaseServiceImpl<IdCardRecordEntity,String> implements IdCardRecordService{

	
	@Autowired
	private IdCardRecordRepository repo;
	
	@Override
	public boolean existsPageId(Long startPageId, Long endPageId) {
		return (repo.selectByPageId(startPageId) == null && repo.selectByPageId(endPageId) == null);
	}

}
