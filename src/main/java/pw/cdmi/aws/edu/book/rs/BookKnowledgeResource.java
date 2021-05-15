package pw.cdmi.aws.edu.book.rs;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.aws.edu.book.modules.entities.BookKnowledgeEntity;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.rs.request.KnowLedgeRequest;
import pw.cdmi.aws.edu.book.services.BookKnowLedgerService;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
public class BookKnowledgeResource {
	
	@Autowired
	BookKnowLedgerService knowService;
	
	@Autowired
	TextBookService bookServie;
	
	
	/**
	 * 新增教材知识点
	 * @param bookid
	 * @param know
	 * @return
	 */
	@PostMapping("book/{bookid}/knowledge")
	public String addKnowledge(@PathVariable("bookid") String bookid,@RequestBody @Validated KnowLedgeRequest know){
		TextbookEntity book = bookServie.getOne(bookid);
		if(book == null) throw  new HttpClientException(ErrorMessages.NotFoundObjectKey);
		BookKnowledgeEntity entity = new BookKnowledgeEntity();
		entity.setBookId(bookid);
		entity.setTitle(know.getTitle());
		entity.setSortOrderValue(knowService.selectMaxSn(bookid) + 1);
		knowService.save(entity);
		return entity.getTitle();
	}
	
	/**
	 * 编辑教材知识点
	 * @param bookid
	 * @param knowledgeid
	 * @param know
	 */
	@PutMapping("book/{bookid}/knowledge/{knowledgeid}")
	public void modifyKnowledge(@PathVariable("bookid") String bookid,@PathVariable("knowledgeid") String knowledgeid,@RequestBody @Validated KnowLedgeRequest req){
		TextbookEntity book = bookServie.getOne(bookid);
		BookKnowledgeEntity entity = knowService.getOne(knowledgeid);
		if(book == null || entity == null || !bookid.equals(entity.getBookId())) throw  new HttpClientException(ErrorMessages.NotFoundObjectKey);
		entity.setBookId(bookid);
		if(req.getSortOrderValue() != null) {
			entity.setSortOrderValue(req.getSortOrderValue());
		}
		if(StringUtils.isNotBlank(req.getTitle())) {
			entity.setTitle(req.getTitle());
		}
		knowService.saveAndFlush(entity);
	}
	
	/**
	 * 删除教材知识点
	 * @param bookid
	 * @param knowledgeid
	 */
	@DeleteMapping("book/{bookid}/knowledge/{knowledgeid}")
	public void delKnowledge(@PathVariable("bookid") String bookid,@PathVariable("knowledgeid") String knowledgeid){
		knowService.deleteById(knowledgeid);
	}
	
	/**
	 * 获取教材知识点列表
	 * @param bookid
	 * @return
	 */
	@GetMapping("book/{bookid}/knowledge")
	public List<BookKnowledgeEntity> addKnowledge(@PathVariable("bookid") String bookid){
		BookKnowledgeEntity e = new BookKnowledgeEntity();
		e.setBookId(bookid);
		Sort sort = Sort.by("sortOrderValue");
		return knowService.findAll(Example.of(e),sort);
	}
	
	
}
