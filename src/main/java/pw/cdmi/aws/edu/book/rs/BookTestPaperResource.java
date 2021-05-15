package pw.cdmi.aws.edu.book.rs;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.aws.edu.book.modules.TextbookFormat;
import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.rs.request.BookClassHourRequest;
import pw.cdmi.aws.edu.book.rs.request.BookTestPaperRequest;
import pw.cdmi.aws.edu.book.rs.request.TestBookRequest;
import pw.cdmi.aws.edu.book.services.BookClassHourService;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.core.exception.HttpClientException;

/**
 * 试卷模块
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/edu/v1")
public class BookTestPaperResource {
	
	@Autowired
	TextBookService bookService;
	
	@Autowired
	BookClassHourService hourService;

	
	/**
	 * 为教材设置试卷
	 * @param req
	 * @return
	 */
	@PutMapping("/book/setTestPaper")
	public void setBookTestBook(@RequestBody TestBookRequest req){
		if(StringUtils.isBlank(req.getBookId()) || StringUtils.isBlank(req.getTestBookId())) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		
		TextbookEntity book = bookService.getOne(req.getBookId());
		if(book == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		TextbookEntity testBook = bookService.getOne(req.getTestBookId());
		if(testBook == null || testBook.getFormat() != TextbookFormat.TEST) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		book.setTestBookId(req.getTestBookId());
		bookService.saveAndFlush(book);
		
	}
	
	/**
	 * 为教材添加测试试卷
	 * @param bookid
	 * @param req
	 * @return
	 */
	@PostMapping("/book/testpaper")
	public void addBookTestPaper(@RequestBody @Validated BookTestPaperRequest req){
		
		
		TextbookEntity check1 = bookService.selectBookByPageno(req.getStartPageno());
    	if(check1 != null ) {
    		throw new HttpClientException(ErrorMessages.PageIdExistsException);
    	}
    	TextbookEntity check2 = bookService.selectBookByPageno(req.getEndPageno());
    	if(check2 != null) {
    		throw new HttpClientException(ErrorMessages.PageIdExistsException);
    	}
		
		TextbookEntity book = bookService.getOne(req.getBookId());
		if(book == null ) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		TextbookEntity testBook = new TextbookEntity();
		testBook.setName(book.getName()+"-试卷");
		testBook.setStartPageno(req.getStartPageno());
		testBook.setEndPageno(req.getEndPageno());
		testBook.setCode(System.currentTimeMillis()+"");
		testBook.setPdfUrl(req.getPdfUrl());
		testBook.setFormat(TextbookFormat.TEST);
		testBook.setPageSize(req.getPageSize());
		testBook.setIdentityRangle(req.getIdentityRangle());
		testBook.setCreateDate(new Date());
		testBook.setScoreRangle(req.getScoreRangle());
		testBook.setCourse(book.getCourse());
		testBook.setGrade(book.getGrade());
		testBook.setSemester(book.getSemester());
		testBook.setStage(book.getStage());
		bookService.save(testBook);
		
		book.setTestBookId(testBook.getId());
		bookService.saveAndFlush(book);
		
		bookService.setBookPageIdInfo(testBook);
		
	}
	
	
	@PutMapping("/book/testpaper")
	public void putBookTestPaper(@RequestBody @Validated BookTestPaperRequest req){
		
		
		TextbookEntity check1 = bookService.selectBookByPageno(req.getStartPageno());
    	if(check1 != null && !check1.getId().equals(req.getTestBookId())) {
    		throw new HttpClientException(ErrorMessages.PageIdExistsException);
    	}
    	TextbookEntity check2 = bookService.selectBookByPageno(req.getEndPageno());
    	if(check2 != null && !check1.getId().equals(req.getTestBookId())) {
    		throw new HttpClientException(ErrorMessages.PageIdExistsException);
    	}
		
    	TextbookEntity testBook = bookService.getOne(req.getTestBookId());
		if(testBook == null || testBook.getFormat() != TextbookFormat.TEST) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		
		testBook.setStartPageno(req.getStartPageno());
		testBook.setEndPageno(req.getEndPageno());
		testBook.setPdfUrl(req.getPdfUrl());
		testBook.setPageSize(req.getPageSize());
		testBook.setIdentityRangle(req.getIdentityRangle());
		testBook.setCreateDate(new Date());
		testBook.setScoreRangle(req.getScoreRangle());
	
		bookService.saveAndFlush(testBook);
		
	
		
		bookService.setBookPageIdInfo(testBook);
		
	}
	
	
	/**
	 * 为试卷添加课时信息
	 * @param bookid
	 * @param req
	 */
	
	@Deprecated
	@PostMapping("/testpaper/{bookid}/classhour")
	public void putBookTestPaperClasshour(@PathVariable("bookid") String bookid,@RequestBody @Validated BookClassHourRequest req){
		
		TextbookEntity book = bookService.getOne(bookid);
		if(book == null ) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		BookClassHourEntity entity = new BookClassHourEntity();
		entity.setBookId(bookid);
		entity.setSubtitle(req.getSubtitle());
		entity.setKnowledges(req.getKnowledgeid());
		entity.setOrderValue(req.getOrderValue());
		hourService.save(entity);
	}
	

	/**
	 *  获取试卷课时列表
	 * @param bookid
	 * @param count
	 */
	@GetMapping("testpaper/{bookid}/classhour")
	public List<BookClassHourEntity> getBookClassHour(@PathVariable("bookid") String bookid){
		BookClassHourEntity e = new BookClassHourEntity();
		e.setBookId(bookid);
		Sort sort = Sort.by("orderValue");
		return hourService.findAll(Example.of(e),sort);
	}
	
	
}
