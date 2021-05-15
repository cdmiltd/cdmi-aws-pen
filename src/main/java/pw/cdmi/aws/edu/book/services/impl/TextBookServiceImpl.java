package pw.cdmi.aws.edu.book.services.impl;

import java.util.List;
import java.util.stream.LongStream;

import org.apache.pdfbox.pdmodel.fdf.FDFPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;
import pw.cdmi.aws.edu.book.modules.PageSize;
import pw.cdmi.aws.edu.book.modules.TextbookFormat;
import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.repo.TextBookRepository;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.Semester;

@Service
public class TextBookServiceImpl extends BaseServiceImpl<TextbookEntity, String> implements TextBookService{

	
	@Autowired
	private TextBookRepository repo;
	
	@Autowired
	private PDFPageServiceImpl pdfPageService;
	
	@Override
	public TextbookEntity selectBookByPageno(Long pageid) {
		return repo.selectBookByPageno(pageid);
	}

	@Override
	public List<TextbookEntity> selectSchoolBook(List<String> ids,String semester,String grade,String course,String stage) {
		
		return repo.selectSchoolBook(ids, semester, grade,course,stage);
	}

	@Override
	public List<TextbookEntity> findByGradeGroupByCourse(GlobalGradeStage globalGradeStage) {
		return repo.findByGradeGroupByCourse(globalGradeStage);
	}

	@Override
	@Transactional
	public void setBookPageIdInfo(TextbookEntity book) {
		PDFTypeEnum type = PDFTypeEnum.Book;
		if(book.getFormat() == TextbookFormat.TEST) {
			type = PDFTypeEnum.Test;
		}
		
		//刪除原有pageid信息
		pdfPageService.deleteSoucePageId(book.getId(), type);
		
		//检查是否存在
		PDFPageEntity startPage = pdfPageService.getByPageId(book.getStartPageno());
		if(startPage != null) {
			throw new RuntimeException("页码:"+book.getStartPageno()+"已经存在!");
		}
		PDFPageEntity endPage = pdfPageService.getByPageId(book.getEndPageno());
		if(endPage != null) {
			throw new RuntimeException("页码:"+book.getEndPageno()+"已经存在!");
		}
		
		repo.saveAndFlush(book);
		
		long[] pageIds = LongStream.rangeClosed(book.getStartPageno(), book.getEndPageno()).toArray();
		for (int i = 0; i < pageIds.length; i++) {
			//创建学生的 page id 信息
			PDFPageEntity s = new PDFPageEntity();
			s.setPageId(pageIds[i]);
			s.setType(type);
			s.setSourceId(book.getId());
			pdfPageService.save(s);	
				
		}
		
	}

	@Override
	@Transactional
	public void deleteBook(TextbookEntity book) {
		//刪除原有pageid信息
		pdfPageService.deleteSoucePageId(book.getId(), PDFTypeEnum.Book);
		
		repo.deleteById(book.getId());
				
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
