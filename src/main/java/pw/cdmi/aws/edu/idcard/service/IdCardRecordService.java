package pw.cdmi.aws.edu.idcard.service;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.idcard.entity.IdCardRecordEntity;

public interface IdCardRecordService extends BaseService<IdCardRecordEntity,String>{

	/**
	 * 判断是否已经存在pageid
	 * @param startPageId
	 * @param endPageId
	 * @return
	 */
	public boolean existsPageId(Long startPageId,Long endPageId);
}
