package pw.cdmi.aws.edu.book.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.repo.BookClassHourRepository;
import pw.cdmi.aws.edu.book.repo.BookKnowledgeRepository;
import pw.cdmi.aws.edu.book.services.BookClassHourService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;

@Service
public class BookClassHourServiceImpl extends BaseServiceImpl<BookClassHourEntity, String> implements BookClassHourService{

	@Autowired
	private BookClassHourRepository repo;
	
	@Autowired
	private BookKnowledgeRepository knowRepo;
	
	@Override
	public void initBookClassHour(String bookId, int count) {
		
		BookClassHourEntity ex = new BookClassHourEntity();
		ex.setBookId(bookId);
		long num = repo.count(Example.of(ex));
		List<BookClassHourEntity> list = new ArrayList<>();
		//第一次初始化教材课时
		if(num == 0) {
			for (int i = 0; i < count; i++) {
				BookClassHourEntity e = new BookClassHourEntity();
				e.setBookId(bookId);
				e.setOrderValue(i+1);
				list.add(e);
			}
		}else {
			int maxOV = repo.selectMaxOrderVal(bookId);
			for (int i = 0; i < count; i++) {
				BookClassHourEntity e = new BookClassHourEntity();
				e.setBookId(bookId);
				e.setOrderValue(maxOV+i+1);
				list.add(e);
			}
			
		}
		
		repo.saveAll(list);
		
		
	}

	@Override
	public Integer selectMaxOrderVal(String bookId) {
		Integer max = repo.selectMaxOrderVal(bookId);
		return max == null ? 0 : max;
	}

	@Override
	public BookClassHourEntity selectBookClassHourByPageNo(Long pageNo) {
		
		return repo.selectBookClassHourByPageNo(pageNo);
	}


}
