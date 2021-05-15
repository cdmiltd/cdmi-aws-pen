package pw.cdmi.aws.edu.book.services;

import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.common.service.BaseService;

public interface BookClassHourService extends BaseService<BookClassHourEntity, String>{

	/**
	 * 设置/初始化教材课时
	 * @param bookId
	 * @param count
	 */
	public void initBookClassHour(String bookId,int count);
	
	public Integer selectMaxOrderVal(String bookId);
	
	public BookClassHourEntity selectBookClassHourByPageNo(Long pageNo);
	
}
