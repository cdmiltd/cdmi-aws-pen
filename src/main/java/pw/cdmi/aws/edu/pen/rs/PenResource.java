package pw.cdmi.aws.edu.pen.rs;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.pen.modules.entities.PenEntities;
import pw.cdmi.aws.edu.pen.rs.response.PenDetailResponse;
import pw.cdmi.aws.edu.pen.rs.response.PenStatisticResponse;
import pw.cdmi.aws.edu.pen.service.PenSerivce;
import pw.cdmi.aws.edu.pen.service.impl.PenServiceImpl;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.TeacherService;
import pw.cdmi.core.exception.HttpClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/edu/v1/pen")
@Tag(name = "edu", description = "教育模块")
public class PenResource {
	
	
	@Autowired
	PenSerivce penSerivce;
	
	@Autowired
	private TeacherService ts;
	
	@Autowired
	private SchoolService ss;

	@Autowired
	private RedisUtil redis;

	/**
	 * 导入笔信息
	 *
	 * @param file
	 * @return 返回上传成功的笔的数量,记录id等信息
	 * @throws IOException 
	 */
	@PostMapping("/import")
	public int importPen(@RequestParam("file") MultipartFile file) throws IOException {
		if(file.isEmpty()){throw new HttpClientException(ErrorMessages.MissRequiredParameter);}
		// excel 文档
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

		List<PenEntities> data = new ArrayList<>();
		// excel 页
		XSSFSheet sheet = workbook.getSheetAt(0);
		Date date = new Date();
		for(Row row:sheet) {
			if(row.getRowNum() == 0) continue;
			row.getCell(0).setCellType(CellType.STRING);
	    	row.getCell(1).setCellType(CellType.STRING);
			String mac = row.getCell(0).getStringCellValue();
			String sn = row.getCell(1).getStringCellValue();
//			String name = row.getCell(2).getStringCellValue();
//			String schoolid = row.getCell(3).getStringCellValue();
//			String teacherid = row.getCell(4).getStringCellValue();
//			String active = row.getCell(5).getStringCellValue();
			if(StringUtils.isBlank(mac) || StringUtils.isBlank(sn)) {
				continue;
			}
			PenEntities e = new PenEntities();
			e.setMac(mac);
			e.setImportDate(date);
			e.setSn(sn);
			e.setActivated(false);
//			if(StringUtils.isNotBlank(name)) e.setName(name);
//			if(StringUtils.isNotBlank(schoolid)) e.setSchoolId(schoolid);
//			if(StringUtils.isNotBlank(teacherid)) e.setTeacherId(teacherid);
//			if(StringUtils.isNotBlank(active)) {
//				if("是".equals(active)) {
//					e.setActivated(true);
//					e.setActiveDate(date);
//				}
//			}
			data.add(e);
		}
		
		return penSerivce.importPen(data);
	}

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	@GetMapping("/batch")
	public Page<PenEntities> listBatchUpload(@RequestParam(name = "pageNo", required = false,defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", required = false,defaultValue = "20") Integer pageSize) {
		Pageable pg = PageRequest.of(pageNo - 1, pageSize);
		return penSerivce.findAll(pg);
	}

	/**
	 * 获取当前系统内笔的统计信息
	 * 
	 * @return
	 */
	@GetMapping("/statistic")
	public PenStatisticResponse getStatistic() {
		long total = penSerivce.count(Example.of(new PenEntities()));
		
		PenEntities p1 = new PenEntities();
		p1.setActivated(true);
		long activeCount = penSerivce.count(Example.of(p1));
		PenStatisticResponse resp = new PenStatisticResponse();
		resp.setTotal(total);
		resp.setActivatCount(activeCount);
		return resp;
	}

	/**
	 * 获取笔的信息
	 * 
	 * @return
	 */
	@GetMapping("/{penid}")
	public PenDetailResponse getPenDetail(@PathVariable("penid") String penId) {
		
		PenEntities pe = penSerivce.getOne(penId);
		if(pe == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		PenDetailResponse res = new PenDetailResponse();
		BeanUtils.copyProperties(pe, res);
		if(StringUtils.isNotBlank(pe.getSchoolId())) {
			PenDetailResponse.Owner ow = new PenDetailResponse.Owner();
			
			SchoolEntity se =  ss.getOne(pe.getSchoolId());
			if(se != null) {
				ow.setId(se.getId());
				ow.setName(se.getName());
			}
			res.setOwner(ow);
		}
		if(StringUtils.isNotBlank(pe.getTeacherId())) {
			PenDetailResponse.EndUser eu = new PenDetailResponse.EndUser();
			
			TeacherEntity te = ts.getOne(pe.getTeacherId());
			if(te != null) {
				eu.setId(te.getId());
				eu.setPhone(te.getPhone());
				eu.setName(te.getName());
			}
			res.setEnduser(eu);
		}
		
		
		return res;
	}

	/**
	 * 根据Mac地址搜索笔
	 * 
	 * @return
	 */
	@GetMapping("/mac")
	public PenDetailResponse listPenRowResponse(@RequestParam("mac") String mac) {
		PenEntities ex = new PenEntities();
		ex.setMac(mac);
		PenEntities pen = penSerivce.findOne(Example.of(ex));
		if(pen == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		PenDetailResponse res = new PenDetailResponse();
		BeanUtils.copyProperties(pen, res);
		if(StringUtils.isNotBlank(pen.getSchoolId())) {
			PenDetailResponse.Owner ow = new PenDetailResponse.Owner();
			
			SchoolEntity se =  ss.getOne(pen.getSchoolId());
			if(se != null) {
				ow.setId(se.getId());
				ow.setName(se.getName());
			}
			res.setOwner(ow);
		}
		if(StringUtils.isNotBlank(pen.getTeacherId())) {
			PenDetailResponse.EndUser eu = new PenDetailResponse.EndUser();
			
			TeacherEntity te = ts.getOne(pen.getTeacherId());
			if(te != null) {
				eu.setId(te.getId());
				eu.setPhone(te.getPhone());
				eu.setName(te.getName());
			}
			res.setEnduser(eu);
		}
		
		return res;
	}

	/**
	 * 切换当前批改学生
	 *
	 * @return
	 */
	@PutMapping("/mac/switchStudent")
	public Boolean switchStudent(@RequestParam("mac") String mac, @RequestParam("studentId") String studentId) {
		if(StringUtils.isBlank(mac)){
			throw new HttpClientException(ErrorMessages.MissRequiredParameter, mac);
		}
		if(StringUtils.isBlank(studentId)){
			throw new HttpClientException(ErrorMessages.MissRequiredParameter, studentId);
		}
		penSerivce.switchStudent(mac, studentId);
		return Boolean.TRUE;
	}

	/**
	 * 获取当前学生编号
	 * @param mac
	 * @return
	 */
	@GetMapping("/mac/currentStudent")
	public String getCurrentStudentId(@RequestParam("mac") String mac){
		String studentId = (String) redis.get(String.format(PenServiceImpl.Pen_Current_Student_Id, mac));
		if (StringUtils.isBlank(studentId)){
			return "";
		}
		return studentId;
	}
}
