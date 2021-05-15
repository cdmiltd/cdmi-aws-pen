package pw.cdmi.aws.edu.book.services;

import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;
import pw.cdmi.aws.edu.common.service.BaseService;

public interface BookKnowLedgerService extends BaseService<BookKnowledgeEntity, String>{

	
	public Integer selectMaxSn(String bookId);
}
