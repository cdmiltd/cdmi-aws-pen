package pw.cdmi.aws.edu.book.services;

import java.util.List;

import pw.cdmi.aws.edu.book.modules.PageSize;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;

public interface TextBookService extends BaseService<TextbookEntity, String>{

	
	
	TextbookEntity selectBookByPageno(Long pageid);
	
	List<TextbookEntity> selectSchoolBook(List<String> ids,String semester,String grade,String course,String stage);

	/**
	 * 查询该年级的所有课程
	 * @param globalGradeStage
	 * @return
	 */
	List<TextbookEntity> findByGradeGroupByCourse(GlobalGradeStage globalGradeStage);
	
	public void setBookPageIdInfo(TextbookEntity book);
	
	public void deleteBook(TextbookEntity book);
}
