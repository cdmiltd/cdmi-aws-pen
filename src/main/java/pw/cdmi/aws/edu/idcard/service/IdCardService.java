package pw.cdmi.aws.edu.idcard.service;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;
import pw.cdmi.aws.edu.idcard.rs.request.IdCardCreateRequest;

public interface IdCardService extends BaseService<IdCardEntity,String>{

	
	
	
	
	public boolean createByClassTeamId(String classTeamId);
	
	
	public Long selectMaxSn();
	
	public IdCardEntity getBySn(Long sn);
	
	
	
	public IdCardEntity getByPageId(Long pageId);
	
}
