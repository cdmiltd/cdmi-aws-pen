package pw.cdmi.aws.edu.common.rs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;
import pw.cdmi.aws.edu.book.services.impl.PDFPageServiceImpl;

@RestController
@RequestMapping("/edu/v1")
public class PDFPageNoController {

	
	@Autowired
	private PDFPageServiceImpl pageService;
	
	
	@RequestMapping("pageNo/exists")
	public boolean exists(Long pageNo) {
		PDFPageEntity entity = pageService.getByPageId(pageNo);
		return entity != null;
	}
}
