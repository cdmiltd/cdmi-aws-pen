package pw.cdmi.aws.edu.school.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.entities.ClassTeacherEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;
import pw.cdmi.aws.edu.school.rs.requests.ClassTeacherRequest;
import pw.cdmi.aws.edu.school.rs.requests.ClassTeamRequest;
import pw.cdmi.aws.edu.school.rs.responses.ClassTeamRowResponse;
import pw.cdmi.aws.edu.school.rs.responses.SchoolDetaiResponse;
import pw.cdmi.aws.edu.school.services.ClassTeacherService;
import pw.cdmi.aws.edu.school.services.ClassTeamService;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.TeacherService;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu-classsteam", description = "班级模块")
public class ClassTeamResource {
	
	 private static final Logger log = LoggerFactory.getLogger(ClassTeamResource.class);

	@Autowired
	private ClassTeamService teamService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private TeacherService teacherService;
	

	/**
	 * 	获取指定学校指定年级下的班级列表
	 * @param schoolid
	 * @param grade
	 * @return
	 */
	@GetMapping(value = "/classteam")
	public List<ClassTeamRowResponse> listClassTeam(@RequestParam("schoolid") String schoolid,
			@RequestParam(name = "grade",required = false) String grade) {
		SchoolClassTeamEntity ex = new SchoolClassTeamEntity();
		ex.setSchoolId(schoolid);
		if(StringUtils.isNotBlank(grade)) {
			ex.setEndYear(DateUtil.getEndYear(GlobalGradeStage.fromName(grade)));
		}
		
		List<SchoolClassTeamEntity> list = teamService.findAll(Example.of(ex));
		 
	    List<ClassTeamRowResponse> resp = new ArrayList<>();
	    
	    for(SchoolClassTeamEntity e:list) {
	    	ClassTeamRowResponse r = new ClassTeamRowResponse();
	    	BeanUtils.copyProperties(e, r);
	    	if(StringUtils.isNotBlank(e.getAdviser()))
				r.setAdviser(teacherService.getOne(e.getAdviser()));
	    	
	    	resp.add(r);
	    }
	    
	    
		return resp;
	}
	
	/**
	 * 	 获取班级信息
	 * @param schoolid
	 * @param grade
	 * @return
	 */
	@GetMapping(value = "/classteam/{classteamid}")
	public ClassTeamRowResponse getClassTeam(@PathVariable("classteamid") String classid) {
		ClassTeamRowResponse res = new ClassTeamRowResponse();
		SchoolClassTeamEntity e =  teamService.getOne(classid);
		if(e == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);		
		BeanUtils.copyProperties(e, res);
		
		SchoolEntity school = schoolService.getOne(e.getSchoolId());
		if(school != null) {
			SchoolDetaiResponse schoolDetail = new SchoolDetaiResponse();
			BeanUtils.copyProperties(school, schoolDetail);
			
			res.setSchoolDetail(schoolDetail);
		}
		return res;
	}


	/**
	 * 为指定学校指定年级创建班级
	 * @param schoolid
	 * @param number
	 * @return
	 */
	@PutMapping("/classteam/{schoolid}/init")
	public void postClassTeam(@PathVariable("schoolid") String schoolid, @RequestBody ClassTeamRequest req) {
		if(req.getGrade() == null || req.getCount() == null || req.getCount() < 1) throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		log.info("req =>{}",req);

		SchoolClassTeamEntity ex = new SchoolClassTeamEntity();
		ex.setSchoolId(schoolid);
		ex.setEndYear(DateUtil.getEndYear(req.getGrade()));
		long count = teamService.count(Example.<SchoolClassTeamEntity>of(ex));
		if(count > 0) {
			throw new HttpClientException(ErrorMessages.RepetitionInitException);
		}
		
		teamService.initClassTeam(schoolid, req.getGrade(), req.getCount());
	}
	
	/**
	 * 为指定学校指定年级新增班级
	 * @param schoolid
	 * @param number
	 * @return
	 */
	@PostMapping("/classteam/{schoolid}")
	public void createClassTeam(@PathVariable("schoolid") String schoolid, @RequestBody ClassTeamRequest req) {
		if(req.getGrade() == null || req.getOrderValue() == null || req.getOrderValue() < 1 ||StringUtils.isBlank(req.getName())) throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		 teamService.createClassTeam(schoolid, req);
	}


	/**
	 * 编辑指定班级信息
	 * @param classteamid
	 * @param schoolid
	 * @param classTeam
	 */
	@PutMapping("/classteam/{classteamid}")
	public void putClassTeam(@PathVariable("classteamid") String classteamid,@RequestBody ClassTeamRequest req) {
		
		SchoolClassTeamEntity entity = teamService.getOne(classteamid);
		if(entity == null || !entity.getSchoolId().equals(req.getSchoolId())) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		if(StringUtils.isNotBlank(req.getName())) {
			entity.setName(req.getName());
		}
	
		if(StringUtils.isNotBlank(req.getAdviserId())) {
			entity.setAdviser(req.getAdviserId());
		}
		
		if(req.getOrderValue() != null && entity.getOrderValue().intValue() != req.getOrderValue().intValue()) {
			SchoolClassTeamEntity e = new SchoolClassTeamEntity();
	    	e.setOrderValue(req.getOrderValue());
	    	e.setStage(entity.getStage());
	    	e.setEndYear(entity.getEndYear());
	    	e.setSchoolId(req.getSchoolId());
	    	SchoolClassTeamEntity exists = teamService.findOne(Example.of(e));
	    	if(exists != null) {
	    		throw new HttpClientException(ErrorMessages.ExistsDataException);
	    	}
	    	
	    	entity.setOrderValue(req.getOrderValue());
		}
		
		teamService.saveAndFlush(entity);
	}
	/**
	 * 为指定班级设置班主任
	 * @param classteamid
	 * @param teacherid
	 */

	@PutMapping("/classteam/{classteamid}/adviser/{teacherid}")
	public void putClassTeamAdviser(@PathVariable("classteamid") String classteamid,
			@PathVariable("teacherid") String teacherid) {
		SchoolClassTeamEntity entity = teamService.getOne(classteamid);
		if(entity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		entity.setAdviser(teacherid);
		teamService.saveAndFlush(entity);
	}
	
	@Autowired
	private ClassTeacherService cts;

	/**
	 * 为指定班级分配学科老师
	 * @param classteamid
	 * @param teachers
	 */
	@PutMapping("/classteam/{classteamid}/teacher")
	public void putClassTeamTeachers(@PathVariable("classteamid") String classteamid, @RequestBody ClassTeacherRequest req) {
		if(StringUtils.isBlank(req.getClassId()) || StringUtils.isBlank(req.getTeacherId()) || req.getCourse() == null) 
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		ClassTeacherEntity entity = new ClassTeacherEntity();
		BeanUtils.copyProperties(req, entity);
		cts.save(entity);
	}
	
	/**
	 * 从班级移除老师
	 * @param classTeamId
	 * @param teacherId
	 */
	@PutMapping("/classteam/remoteTeacher")
	public void remoteClassTeamTeacher(@RequestParam("classTeamId")String classTeamId,@RequestParam("teacherId")String teacherId) {
		
		ClassTeacherEntity entity = new ClassTeacherEntity();
		entity.setClassId(classTeamId);
		entity.setTeacherId(teacherId);
		ClassTeacherEntity old = cts.findOne(Example.of(entity));
		if(old != null) {
			cts.delete(old);
		}
	}
	
	
	/**
	 * 获取班级学科老师列表
	 * @param classteamid
	 * @param teachers
	 */
	@GetMapping("/classteam/{classteamid}/teacher")
	public List<TeacherEntity> putClassTeamTeachers(@PathVariable("classteamid") String classteamid, @RequestParam(name="course") DefaultCourse course) {
		
		ClassTeacherEntity cte = new ClassTeacherEntity();
		cte.setClassId(classteamid);
		cte.setCourse(course);
		
		List<ClassTeacherEntity> ctlist = cts.findAll(Example.of(cte));
		if(!ctlist.isEmpty()) {
			List<String> ids = ctlist.stream().map(ClassTeacherEntity::getTeacherId).collect(Collectors.toList());
			return teacherService.findAllById(ids);
			
		}
		
		return new ArrayList<>();
		
	}
	
	
	
	


}
