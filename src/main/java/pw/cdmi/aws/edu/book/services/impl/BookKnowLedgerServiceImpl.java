package pw.cdmi.aws.edu.book.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;
import pw.cdmi.aws.edu.book.repo.BookKnowledgeRepository;
import pw.cdmi.aws.edu.book.services.BookKnowLedgerService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;

@Service
public class BookKnowLedgerServiceImpl extends BaseServiceImpl<BookKnowledgeEntity, String> implements BookKnowLedgerService{

	@Autowired
	private BookKnowledgeRepository repo;
	
	@Override
	public Integer selectMaxSn(String bookId) {
		Integer max =repo.selectMaxOrderVal(bookId);
		return max == null ? 0 : max;
	}

}
