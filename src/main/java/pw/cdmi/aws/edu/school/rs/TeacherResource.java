package pw.cdmi.aws.edu.school.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.entities.ClassTeacherEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;
import pw.cdmi.aws.edu.school.rs.requests.DeleteTeacherRequest;
import pw.cdmi.aws.edu.school.rs.requests.TeacherRequest;
import pw.cdmi.aws.edu.school.rs.responses.ClassTeamRowResponse;
import pw.cdmi.aws.edu.school.rs.responses.SchoolDetaiResponse;
import pw.cdmi.aws.edu.school.rs.responses.TeacherRowResponse;
import pw.cdmi.aws.edu.school.services.ClassTeacherService;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.TeacherService;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu-teacher", description = "老师模块")
public class TeacherResource {

	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SchoolService schoolService;
	
	/**
	 * 获取学校指定学部下的学科老师列表
	 * @param schoolid
	 * @param stage
	 * @param course
	 * @return
	 */
	@GetMapping("/teacher")
	public List<TeacherEntity> listTeacher(@RequestParam("schoolid") String schoolid,
			@RequestParam(name="stage",required = false) StudyStage stage, @RequestParam(name = "course", required = false) DefaultCourse course) {
		if (StringUtils.isBlank(schoolid)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		TeacherEntity e = new TeacherEntity();
		e.setSchoolId(schoolid);
		if(stage != null) {
			e.setStage(stage);
		}
		if(course != null) {
			e.setCourse(course);
		}
		List<TeacherEntity> entities = teacherService.findAll(Example.of(e));
		return entities;
	}

	/**
	 * 为指定学校新增老师信息
	 * @param schoolid
	 * @param teacher
	 * @return
	 */
	@PostMapping("/teacher")
	public Boolean postTeacher(@RequestParam("schoolid") String schoolid, @RequestBody @Validated TeacherRequest teacher) {
		if (teacher == null || StringUtils.isBlank(teacher.getName())  || StringUtils.isBlank(teacher.getPhone())) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter, "教师名称");
		}
		
		SchoolEntity se = schoolService.getOne(schoolid);
		if(se == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		TeacherEntity tmp = teacherService.getByPhone(teacher.getPhone());
		if(tmp != null) throw new HttpClientException(ErrorMessages.ExistsDataException);
		
		TeacherEntity entity = new TeacherEntity();
		entity.setName(teacher.getName());
		entity.setSex(teacher.getSex());
		entity.setAge(teacher.getAge());
		entity.setFaceImage(teacher.getFaceImage());
		entity.setStage(teacher.getStage());
		entity.setSchoolId(schoolid);
		entity.setCourse(teacher.getCourse());
		// TODO 校验是否是电话号码
		entity.setPhone(teacher.getPhone());
		
		TeacherEntity exists = teacherService.getByPhone(teacher.getPhone());
		if(exists != null) throw new HttpClientException(ErrorMessages.ExistsDataException);
		
		return teacherService.createTeacher(entity);
	}

	/**
	 * 编辑指定老师信息
	 * @param teacherid
	 * @param req
	 */
	@PutMapping("/teacher/{teacherid}")
	public void putTeacher(@PathVariable("teacherid") String teacherid, @RequestBody TeacherRequest req) {
		if (StringUtils.isBlank(teacherid) || req == null) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		TeacherEntity old = teacherService.getOne(teacherid);
		if (old == null) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey, "教师ID[" + teacherid + "]");
		}
		if (!StringUtils.isBlank(req.getName())) {
			old.setName(req.getName());
		}
		if(req.getAge() != null) {
			old.setAge(req.getAge());
		}
		if(StringUtils.isNotBlank(req.getFaceImage())) {
			old.setFaceImage(req.getFaceImage());
		}
		if(req.getSex() != null) {
			old.setSex(req.getSex());
		}
		if(req.getCourse() != null) {
			old.setCourse(req.getCourse());
		}
		if(req.getStage() != null) {
			old.setStage(req.getStage());
		}
		
		
		teacherService.updateTeacher(old,req.getPhone());
	}

	/**
	 * 获取老师详情
	 * @param teacherid
	 * @return
	 */
	@GetMapping("/teacher/{teacherid}")
	public TeacherRowResponse getTeacherDetail(@PathVariable("teacherid") String teacherid) {
		if (StringUtils.isBlank(teacherid)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		TeacherEntity te = teacherService.getOne(teacherid);
		if(te == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		TeacherRowResponse resp = new TeacherRowResponse();
		
		BeanUtils.copyProperties(te, resp);
		if(StringUtils.isNotBlank(te.getSchoolId())) {
			SchoolDetaiResponse school = new SchoolDetaiResponse();
			SchoolEntity e = schoolService.getOne(te.getSchoolId());
			BeanUtils.copyProperties(e, school);
			resp.setSchool(school);
		}
		
		return resp;
	}
	
	/**
	 * 获取老师所在的学校
	 * @param teacherid
	 * @return
	 */
	@GetMapping("/teacher/{teacherid}/school")
	public SchoolDetaiResponse teacherSchool(@PathVariable("teacherid") String teacherid) {
		if (StringUtils.isBlank(teacherid)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		TeacherEntity te = teacherService.getOne(teacherid);
		
		SchoolDetaiResponse school = new SchoolDetaiResponse();
		
		if(StringUtils.isNotBlank(te.getSchoolId())) {
			SchoolEntity e = schoolService.getOne(te.getSchoolId());
			if(e != null)
			BeanUtils.copyProperties(e, school);

		}
		
		return school;
		
	}

	@Autowired
	private ClassTeacherService cts;
	
	@Autowired
	private SchoolClassTeamService scts;
	
	/**
	 * 获取指定老师负责的班级列表
	 * @param teacherid
	 * @return
	 */
	@GetMapping("/teacher/{teacherid}/classteam")
	public List<ClassTeamRowResponse> getClassTeam4Teacher(@PathVariable("teacherid") String teacherid) {
		if (StringUtils.isBlank(teacherid)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		
		ClassTeacherEntity te = new ClassTeacherEntity();
		te.setTeacherId(teacherid);
		List<ClassTeacherEntity> ctlist = cts.findAll(Example.of(te));
		if(!ctlist.isEmpty()) {
			List<String> ids = ctlist.stream().map(ClassTeacherEntity::getClassId).collect(Collectors.toList());
			
			List<SchoolClassTeamEntity> data = scts.findAllById(ids);
			
			return ListCopy.copyListProperties(data, ClassTeamRowResponse::new);
		}
		
		return new ArrayList<>();
	}

	/**
	 * 删除指定学校指定老师信息
	 * @param teacherid
	 */
	@DeleteMapping("/teacher")
	public void deleteTeacher(@RequestBody  DeleteTeacherRequest req) {
		if (StringUtils.isBlank(req.getTeacherId()) || StringUtils.isBlank(req.getName())) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		TeacherEntity teacher = teacherService.getOne(req.getTeacherId());
		if(teacher == null || !req.getName().trim().equals(teacher.getName())) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		}
		
		teacherService.deleteTeacher(teacher);
	}
}
