package pw.cdmi.aws.edu.school.rs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.aws.edu.pen.modules.entities.PenEntities;
import pw.cdmi.aws.edu.pen.service.PenSerivce;
import pw.cdmi.aws.edu.school.modules.entities.SchoolBookEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.rs.responses.TotalResponse;
import pw.cdmi.aws.edu.school.services.SchoolBookService;
import pw.cdmi.aws.edu.school.services.SchoolService;

@RestController
@RequestMapping("/edu/v1")
public class TotalResource {

	@Autowired
	private PenSerivce penSerivce;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private SchoolBookService sbs;
	
	
	/**
	 * 系统学校 笔 数量统计
	 * @return
	 */
	@GetMapping("/system/total")
	public TotalResponse total() {
		TotalResponse resp = new TotalResponse();
		
		long schoolTotal = schoolService.count(Example.of(new SchoolEntity()));
		PenEntities ex = new PenEntities();
		long penTotal = penSerivce.count(Example.of(ex));
		ex.setActivated(true);
		long penActivatedTotal = penSerivce.count(Example.of(ex));
		
		long bookTotal = sbs.count(Example.of(new SchoolBookEntity()));
		
		resp.setBookTotal(bookTotal);
		resp.setPenActivatedTotal(penActivatedTotal);
		resp.setSchoolTotal(schoolTotal);
		resp.setPenTotal(penTotal);
		
		return resp;
	}
}
