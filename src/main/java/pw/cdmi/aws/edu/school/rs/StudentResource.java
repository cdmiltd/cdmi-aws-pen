package pw.cdmi.aws.edu.school.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.enums.Sex;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderInfo;
import pw.cdmi.aws.edu.guarder.service.impl.InnerJoinService;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.rs.requests.ExportStudentEntity;
import pw.cdmi.aws.edu.school.rs.requests.StudentRequest;
import pw.cdmi.aws.edu.school.rs.responses.GuarderResponse;
import pw.cdmi.aws.edu.school.rs.responses.StudentCourseResponse;
import pw.cdmi.aws.edu.school.rs.responses.StudentRowResponse;
import pw.cdmi.aws.edu.school.services.ClassTeamService;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu-student", description = "学生块")
public class StudentResource {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ClassTeamService classTeamService;
	
	 @Autowired
	 private SchoolClassTeamService classService;

	@Autowired
	private TextBookService textBookService;
	
	
	@Autowired
	InnerJoinService ijSerivce;
	
	/**
	 * 导入班级学生信息列表
	 * 
	 * @param classteamid
	 * @param file
	 * @throws Exception 
	 */
	@PutMapping("/student/import")
	public Integer importStudent(@RequestParam("classteamid") String classteamid,  @RequestParam("file") MultipartFile file) throws Exception {
		if(file.isEmpty()){throw new HttpClientException(ErrorMessages.MissRequiredParameter);}
		//excel 文档
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		
		//excel 页
		XSSFSheet sheet = workbook.getSheetAt(0);
		List<ExportStudentEntity> data = new ArrayList<>();
		for(Row row:sheet) {
	    	if(row.getRowNum() == 0) continue;
	    	ExportStudentEntity e = new ExportStudentEntity();
	    	Cell c0 =row.getCell(0);
	    	if(c0 != null) {
	    		c0.setCellType(CellType.STRING);
	    		e.setName(c0.getStringCellValue());
	    	}
	  
	    	
	    	Cell c1 =row.getCell(1);
	    	if(c1 != null) {
	    		c1.setCellType(CellType.STRING);
	    		e.setSn(c1.getStringCellValue());
	    	}
	    	
	    	Cell c2 =row.getCell(2);
	    	if(c2 != null) {
	    		c2.setCellType(CellType.STRING);
	    		String sex = c2.getStringCellValue();
		    	e.setSex("男".equals(sex) ? Sex.man : Sex.wuman);
	    	}
	    	
	    	Cell c3 =row.getCell(3);
	    	if(c3 != null) {
	    		c3.setCellType(CellType.STRING);
	    		e.setBirthday(c3.getStringCellValue());
	    	}
	    	
	    	Cell c4 =row.getCell(4);
	    	if(c4 != null) {
	    		c4.setCellType(CellType.STRING);
	    		e.setGuarderName(c4.getStringCellValue());
	    	}
	    	
	    	Cell c5 =row.getCell(5);
	    	if(c5 != null) {
	    		c5.setCellType(CellType.STRING);
	    		e.setGuarderPhone(c5.getStringCellValue());
	    	}
	    	Cell c6 =row.getCell(6);
	    	if(c6 != null) {
	    		c6.setCellType(CellType.STRING);
	    		e.setRelation(c6.getStringCellValue());
	    	}
	    	Cell c7 =row.getCell(7);
	    	if(c7 != null) {
	    		c7.setCellType(CellType.STRING);
	    	 	e.setGuarderName2(c7.getStringCellValue());
	    	}
	    	Cell c8 =row.getCell(8);
	    	if(c8 != null) {
	    		c8.setCellType(CellType.STRING);
	    		e.setGuarderPhone2(c8.getStringCellValue());
	    	}
	    	Cell c9 =row.getCell(9);
	    	if(c9 != null) {
	    		e.setRelation2(c9.getStringCellValue());
	    	}
	    	
	    	data.add(e);
	     }
		workbook.close();
		int i = studentService.exportStudent(classteamid, data);
		classService.updateStudentNum(classteamid);
		return i;
	}
	
	

	/**
	 * 向班级中添加一个学生信息
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/student")
	public String postStudent(@RequestBody StudentRequest req) {
		
		
		StudentEntity se = new StudentEntity();
		se.setSn(req.getSn());
		se.setClassteamid(req.getClassteamid());
		List<StudentEntity> list =  studentService.findAll(Example.of(se));
		if(!list.isEmpty()) throw new HttpClientException(ErrorMessages.ExistsDataException);
		String id = studentService.addStudent(req);
		classService.updateStudentNum(req.getClassteamid());
		return id;
	}

	/**
	 * 编辑指定学生信息
	 * @param studentid
	 * @param req
	 */
	@PutMapping("/student/{studentid}")
	public void putStudent(@PathVariable("studentid") String studentid,@RequestBody StudentRequest req) {
		StudentEntity old = studentService.getOne(studentid);
		if(old == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		//如果学号不相等
		if(req.getSn() != null) {
			if(!req.getSn().equals(old.getSn())) {
				StudentEntity se = new StudentEntity();
				se.setSn(req.getSn());
				se.setClassteamid(old.getClassteamid());
				List<StudentEntity> list =  studentService.findAll(Example.of(se));
				if(!list.isEmpty()) throw new HttpClientException(ErrorMessages.ExistsDataException);
			}
			old.setSn(req.getSn());
		}
		if(StringUtils.isNotBlank(req.getName())) {
			old.setName(req.getName());
		}
		if(StringUtils.isNotBlank(req.getBirthday())) {
			old.setBirthday(req.getBirthday());
		}
		if(req.getSex() != null) {
			old.setSex(req.getSex());
		}
		
		studentService.saveAndFlush(old);
	}

	/**
	 * 获取指定学生信息
	 * @param studentid
	 * @return
	 */
	@GetMapping("/student/detail/{studentid}")
	public StudentEntity getStudentDetail(@PathVariable("studentid") String studentid) {
		return studentService.getOne(studentid);
	}

	/**
	 * 获取指定班级下的学生列表
	 * @param classteamid
	 * @return
	 */
	@GetMapping("/student")
	public List<StudentRowResponse> listStudent(@RequestParam("classteamid") String classteamid,@RequestParam("schoolid")String schoolid) {
		StudentEntity ex = new StudentEntity();
		ex.setClassteamid(classteamid);
		ex.setSchoolId(schoolid);
		List<StudentEntity> list = studentService.findAll(Example.of(ex));
		if(list.isEmpty()) {
			return new ArrayList<>();
		}
		
		Map<String, List<GuarderResponse>>  map = getStudentGuarder(list);
		
		List<StudentRowResponse> resp = ListCopy.copyListProperties(list, StudentRowResponse::new);
		resp.forEach(e->{
			e.setGuarders(map.get(e.getId()));
		});
		
		
		return resp;
	}
	
	private Map<String, List<GuarderResponse>> getStudentGuarder(List<StudentEntity> list) {
		List<String> studentIds = list.stream().map(StudentEntity::getId).collect(Collectors.toList());
		List<GuarderInfo> rs = ijSerivce.selectGuarderInfo(studentIds);
		List<GuarderResponse> grList = ListCopy.copyListProperties(rs, GuarderResponse::new);
		return grList.stream().collect(Collectors.groupingBy(GuarderResponse::getStudentId));
	}


	/**
	 * 获取学生所有课程信息
	 * @param studentId
	 */
	@GetMapping("/student/course")
	public List<StudentCourseResponse> findStudentCourse(@RequestParam String studentId) {
		if(StringUtils.isBlank(studentId)) throw new HttpClientException(ErrorMessages.MissRequiredParameter, studentId);
		List<StudentCourseResponse> scr = new ArrayList<>();
		StudentEntity studentEntity = studentService.getOne(studentId);
		if(studentEntity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, studentId);
		SchoolClassTeamEntity sct = classTeamService.getOne(studentEntity.getClassteamid());
		if(sct == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey, studentEntity.getClassteamid());
		GlobalGradeStage ggs =DateUtil.getGrade(sct.getEndYear(), sct.getStage());

		List<TextbookEntity> textbookEntities = textBookService.findByGradeGroupByCourse(ggs);
		for (TextbookEntity te: textbookEntities) {
			StudentCourseResponse studentCourseResponse = new StudentCourseResponse();
			studentCourseResponse.setCourse(te.getCourse());
			studentCourseResponse.setCourseName(te.getCourseText());
			scr.add(studentCourseResponse);
		}
		return scr;
	}

	/**
	 * 删除一个班级学生信息
	 * @param studentid
	 */
	@DeleteMapping("/student/{studentid}")
	public void deleteStudent(@PathVariable("studentid") String studentid) {
		StudentEntity student = studentService.getOne(studentid);
		if(student != null) {
			studentService.deleteById(studentid);
		}
		classService.updateStudentNum(student.getClassteamid());
	}
}
