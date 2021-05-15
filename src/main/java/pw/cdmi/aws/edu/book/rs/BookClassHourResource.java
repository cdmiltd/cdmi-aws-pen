package pw.cdmi.aws.edu.book.rs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import pw.cdmi.aws.edu.book.modules.entities.BookClassHourEntity;
import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.rs.request.BookClassHourRequest;
import pw.cdmi.aws.edu.book.rs.request.ClassHourPagesRequest;
import pw.cdmi.aws.edu.book.rs.request.CreateClassHourRequest;
import pw.cdmi.aws.edu.book.rs.response.ClassHourResponse;
import pw.cdmi.aws.edu.book.rs.response.KnowledgeResponse;
import pw.cdmi.aws.edu.book.services.BookClassHourService;
import pw.cdmi.aws.edu.book.services.BookKnowLedgerService;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.core.exception.ErrorMessage;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
public class BookClassHourResource {

	@Autowired
	private BookClassHourService classHourService;
	
	@Autowired
	private TextBookService bookService;
	
	@Autowired
	private BookKnowLedgerService knowService;
	
	/**
	 * 设置教材课时
	 * @param bookid
	 * @param count
	 */
	@PutMapping("book/{bookid}/classhour")
	public void setBookClassHour(@PathVariable("bookid") String bookid,@RequestBody BookClassHourRequest req){
		TextbookEntity book = bookService.getOne(bookid);
		if(book == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey); 
		classHourService.initBookClassHour(bookid, req.getNum());
	}
	
	@PostMapping("book/classhour/create")
	public void createClassHour(@RequestBody CreateClassHourRequest req){
		
		if(StringUtils.isBlank(req.getBookId()) || StringUtils.isBlank(req.getKnowledges()) || StringUtils.isBlank(req.getSubtitle()) 
				|| req.getBeginPageNo() == null || req.getEndPageNo() == null || req.getOrderValue() == null)
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		if(req.getBeginPageNo() > req.getEndPageNo()) {
			throw new HttpClientException(ErrorMessages.ArgValidException);
		}
		
		
		BookClassHourEntity entity = new BookClassHourEntity();
		
		BeanUtils.copyProperties(req, entity,"id");

		classHourService.save(entity);
		
	}
	
	@PutMapping("book/classhour/create")
	public void updateClassHour(@RequestBody CreateClassHourRequest req){
		
		if(StringUtils.isBlank(req.getId()) || StringUtils.isBlank(req.getKnowledges()) || StringUtils.isBlank(req.getSubtitle()) 
				|| req.getBeginPageNo() == null || req.getEndPageNo() == null || req.getOrderValue() == null)
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		if(req.getBeginPageNo() > req.getEndPageNo()) {
			throw new HttpClientException(ErrorMessages.ArgValidException);
		}
		
		BookClassHourEntity old = classHourService.getOne(req.getId());
		if(old ==null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		BeanUtils.copyProperties(req, old,"bookId");
		
		classHourService.save(old);
		
	}
	
	
	
	
	
	/**
	 *  获取教材课时列表
	 * @param bookid
	 * @param count
	 */
	@GetMapping("book/{bookid}/classhour")
	public List<ClassHourResponse> getBookClassHour(@PathVariable("bookid") String bookid){
		BookClassHourEntity ex = new BookClassHourEntity();
		ex.setBookId(bookid);
		Sort sort = Sort.by("orderValue");
		List<BookClassHourEntity> list =classHourService.findAll(Example.of(ex),sort);
		List<ClassHourResponse> resp = new ArrayList<ClassHourResponse>();
		
		list.forEach(e->{
			ClassHourResponse r = new ClassHourResponse();
			BeanUtils.copyProperties(e, r);
			if(StringUtils.isNotBlank(e.getKnowledges())) {
				KnowledgeResponse kr = new KnowledgeResponse();
				BookKnowledgeEntity knowled = knowService.getOne(e.getKnowledges());
				if(knowled != null) {
					BeanUtils.copyProperties(knowled, kr);
					r.setKnow(kr);
				}
			}
			resp.add(r);
		});
		
		return resp;
	}
	
	/**
	 * 为制定教材课时设置知识点
	 * @param bookid
	 * @param count
	 */
	@PutMapping("book/{bookid}/classhour/{classhourid}")
	public void  setBookClassHourKnowledge(@PathVariable("bookid") String bookid,@PathVariable("classhourid") String classhourid,@RequestBody  BookClassHourRequest req){
		TextbookEntity book = bookService.getOne(bookid);
		if(book == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey); 
		BookClassHourEntity e = classHourService.getOne(classhourid);
		if(e == null || !bookid.equals(e.getBookId())) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		BookKnowledgeEntity knowEntity = knowService.getOne(req.getKnowledgeid());
		if(knowEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		e.setKnowledges(req.getKnowledgeid());
		
		classHourService.saveAndFlush(e);
	}
	
	
	@DeleteMapping("book/classhour/{classhourid}")
	public void  delBookClassHour(@PathVariable("classhourid") String classhourid){
		BookClassHourEntity e = classHourService.getOne(classhourid);
		if(e != null) {
			classHourService.deleteById(classhourid);
		}
	}
	
		
}
