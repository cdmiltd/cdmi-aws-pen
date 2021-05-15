package pw.cdmi.aws.edu.school.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.services.AppUserService;
import pw.cdmi.aws.edu.pen.modules.entities.PenEntities;
import pw.cdmi.aws.edu.pen.service.PenSerivce;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.SchoolType;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolStudyStageEntity;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;
import pw.cdmi.aws.edu.school.rs.requests.SchoolRequest;
import pw.cdmi.aws.edu.school.rs.responses.ClassTeamRowResponse;
import pw.cdmi.aws.edu.school.rs.responses.GradeResponse;
import pw.cdmi.aws.edu.school.rs.responses.SchoolDetaiResponse;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.SchoolStudyStageService;
import pw.cdmi.aws.edu.school.services.TeacherService;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu", description = "教育模块")
public class SchoolResource {
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private SchoolStudyStageService stageService;
	
	@Autowired
	private SchoolClassTeamService classSerivce;
	
	@Autowired
	private PenSerivce penService;
	
	@Autowired
	private TeacherService teacherService;

	
	@Autowired
	private AppUserService userService;

	@Value("${cdmi.aws.appId}")
	private String appId;
	
	@Value("${huawei.obs.north.endPoint}")
	private String endPoint;
	
	@Value("${huawei.obs.public.bucketName}")
	private String publicBucket;
	
	
	
	@GetMapping("/school/search")
	public List<SchoolDetaiResponse> searchSchool(@RequestParam(name = "district", required = false) String district,@RequestParam(name = "city", required = false) String city,
			@RequestParam(name = "stage", required = false) String stage,@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "manageId", required = false) String manageId) {
		SchoolEntity ex = new SchoolEntity();
		ExampleMatcher em = ExampleMatcher.matching();
		if(StringUtils.isNotBlank(stage)) {
			em = em.withMatcher("stages", m->m.contains());
			ex.setStages(stage);
		}
		
		if(StringUtils.isNotBlank(district)) {
			em = em.withMatcher("districtCode", m->m.exact());
			ex.setDistrictCode(district);
		}
		if(StringUtils.isNotBlank(city)) {
			em = em.withMatcher("cityCode", m->m.exact());
			ex.setCityCode(city);
		}
		if(StringUtils.isNotBlank(name)) {
			em = em.withMatcher("name", m->m.contains());
			ex.setName(name);
		}
		if(StringUtils.isNotBlank(manageId)) {
			em = em.withMatcher("manageId", m->m.exact());
			ex.setManageId(manageId);
		}
		List<SchoolDetaiResponse> resp = new ArrayList<>();
		List<SchoolEntity> list = schoolService.findAll(Example.of(ex,em));
		for (SchoolEntity e : list) {
			SchoolDetaiResponse r = new SchoolDetaiResponse();
			BeanUtils.copyProperties(e, r);
			
			PenEntities pex = new PenEntities();
			pex.setSchoolId(e.getId());
			r.setPenTotal(penService.count(Example.of(pex)));
			
			resp.add(r);
		}
		
		return resp;
	}
	

	@GetMapping("/school")
	public List<SchoolDetaiResponse> listSchool(@RequestParam(name = "district", required = false) String district,@RequestParam(name = "city", required = false) String city,
			@RequestParam(name = "stage", required = false) String stage,@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "manageId", required = false) String manageId) {
		
		

		SchoolEntity ex = new SchoolEntity();
		ExampleMatcher em = ExampleMatcher.matching();
		if(StringUtils.isNotBlank(stage)) {
			em = em.withMatcher("stages", m->m.contains());
			ex.setStages(stage);
		}
		
		if(StringUtils.isNotBlank(district)) {
			em = em.withMatcher("districtCode", m->m.exact());
			ex.setDistrictCode(district);
		}
		if(StringUtils.isNotBlank(city)) {
			em = em.withMatcher("cityCode", m->m.exact());
			ex.setCityCode(city);
		}
		if(StringUtils.isNotBlank(name)) {
			em = em.withMatcher("name", m->m.contains());
			ex.setName(name);
		}
		if(StringUtils.isNotBlank(manageId)) {
			em = em.withMatcher("manageId", m->m.exact());
			ex.setManageId(manageId);
		}
		
		List<SchoolEntity> elist = schoolService.findAll(Example.of(ex,em));
		
		
		
		List<SchoolDetaiResponse> resp = new ArrayList<>();
		
		List<String> mIds = elist.stream().map(SchoolEntity::getManageId).collect(Collectors.toList());
		
		List<AppUserEntity> manageList = userService.findAllById(mIds);
		
		Map<String,AppUserEntity> mumap = manageList.stream().collect(Collectors.toMap(AppUserEntity::getId, e->e));
		
		for (SchoolEntity e : elist) {
			SchoolDetaiResponse dr = new SchoolDetaiResponse();
			BeanUtils.copyProperties(e, dr);
			PenEntities pex = new PenEntities();
			pex.setSchoolId(e.getId());
			dr.setPenTotal(penService.count(Example.of(pex)));
			if(StringUtils.isNotBlank(e.getManageId())) {
				AppUserEntity tmp =	mumap.get(e.getManageId());
				if(tmp != null) {
					dr.setMgrName(tmp.getTrueName());
					dr.setMgrPhone(tmp.getPhone());
				}
			}

			resp.add(dr);
		}
		
		
		

		return resp;
	}

	/**
	 * 创建一个学校
	 * @param newSchool
	 * @return
	 */
	@PostMapping("/school")
	public String postSchool(@RequestBody SchoolRequest newSchool) {
		/** 创建一个学校，根据学校是否是（义务教学学校），是查找学校所拥有的学业范围，如果是小学，初中，高中，则自动为学校创建其年级和学科 **/
		if (newSchool == null || StringUtils.isBlank(newSchool.getName()) || StringUtils.isBlank(newSchool.getCityId())
				|| StringUtils.isBlank(newSchool.getDistrictId())) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		SchoolEntity entity = new SchoolEntity();
		entity.setAppId(appId);
		entity.setType(SchoolType.FullTimeSchool); // 默认为全日制学校
		entity.setName(newSchool.getName());
		entity.setBrief(newSchool.getBrief());
		entity.setCityCode(newSchool.getCityId());
		entity.setDistrictCode(newSchool.getDistrictId());
		if(StringUtils.isNotBlank(newSchool.getImage())) {
			StringBuffer sb = new StringBuffer();
			sb.append("https://").append(publicBucket).append(".").append(endPoint).append("/").append(newSchool.getImage());
			entity.setImage(sb.toString());
		}
		
		schoolService.createsSchool(newSchool.getStages(),entity);
		
		return entity.getId();
	}

	/**
	 * 编辑学校信息
	 * @param schoolid
	 * @param editSchool
	 */
	@PutMapping("/school/{schoolid}")
	public void putSchool(@PathVariable("schoolid") String schoolid, @RequestBody SchoolRequest editSchool) {
		if (StringUtils.isBlank(schoolid) || editSchool == null) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		SchoolEntity old = schoolService.getOne(schoolid);
		if (old == null) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey, schoolid);
		}
		if (StringUtils.isNotBlank(editSchool.getName())) {
			old.setName(editSchool.getName());
		}
		if (StringUtils.isNotBlank(editSchool.getBrief())) {
			old.setBrief(editSchool.getBrief());
		}
		if (StringUtils.isNotBlank(editSchool.getCityId())) {
			old.setCityCode(editSchool.getCityId());
		}
		if (StringUtils.isNotBlank(editSchool.getDistrictId())) {
			old.setDistrictCode(editSchool.getDistrictId());
		}
		if(StringUtils.isNotBlank(editSchool.getImage())) {
			StringBuffer sb = new StringBuffer();
			sb.append("https://").append(publicBucket).append(".").append(endPoint).append("/").append(editSchool.getImage());
			old.setImage(sb.toString());
		}
		
		schoolService.updateSchool(editSchool.getStages(), old);
	}

	

	/**
	 * 获取学校的可选择学部列表
	 * @param schoolid
	 * @return
	 */
	@GetMapping("/school/{schoolid}/stage")
	public List<SchoolStudyStageEntity> getSchoolGrade(@PathVariable("schoolid") String schoolid) {
		if (StringUtils.isBlank(schoolid)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		SchoolStudyStageEntity e = new SchoolStudyStageEntity();
		e.setSchoolid(schoolid);
		return stageService.findAll(Example.of(e));
	}

	
	/**
	 * 为学校设置管理员
	 * @param schoolid
	 * @param name
	 * @param phone
	 */
	@PutMapping("/school/{schoolid}/manager")
	public void setSchoolManager(@PathVariable("schoolid") String schoolid, @RequestParam("name") String name,
			@RequestParam("phone") String phone) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(phone)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		// 电话号码格式检查
		if (!Pattern.matches("^1[3-9]\\d{9}$", phone.trim())) {
			throw new HttpClientException(ErrorMessages.WrongPhoneNumberFormat, phone);
		}
		
		schoolService.addManage(schoolid, name, phone);
		
//		SmnService.sendMessage(phone, "系统设置你为["+school.getName()+"]管理员,请使用此手机号用登录系统!(如未设置密码可用短信验证码登录)");
	}

	/**
	 * 获取指定学校信息
	 * @param schoolid
	 * @return
	 */
	@GetMapping("/school/{schoolid}")
	public SchoolDetaiResponse getSchoolDetail(@PathVariable("schoolid") String schoolid) {
		if (StringUtils.isBlank(schoolid)) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		SchoolEntity entity = schoolService.getOne(schoolid);
		if(entity == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		SchoolDetaiResponse res = new SchoolDetaiResponse();
		BeanUtils.copyProperties(entity, res);
		
		if(StringUtils.isNotBlank(entity.getManageId())) {
			AppUserEntity mgr = userService.getOne(entity.getManageId());
			res.setMgrName(mgr.getTrueName());
			res.setMgrPhone(mgr.getPhone());
		}
		
		
		return res;
	}

	/**
	 *  删除一个空的学校信息
	 * @param schoolid
	 */
	@DeleteMapping("/school")
	public void deleteSchool(@RequestBody SchoolRequest req) {
		if (StringUtils.isBlank(req.getSchoolId()) || StringUtils.isBlank(req.getName())) {
			throw new HttpClientException(ErrorMessages.MissRequiredParameter);
		}
		
		SchoolEntity entity = schoolService.getOne(req.getSchoolId());
		if(entity == null || !req.getName().trim().equalsIgnoreCase(entity.getName())) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		}
		
		
		TeacherEntity ex = new TeacherEntity();
		ex.setSchoolId(req.getSchoolId());
		long count = teacherService.count(Example.of(ex));
		if(count == 0) {
			schoolService.deleteById(req.getSchoolId());
    	}else {
    		throw new HttpClientException(ErrorMessages.DeleteDataException);
    	}
		
	}


	/**
	 * 获取指定学校的年级列表
	 * @param schoolid
	 * @return
	 */
	@GetMapping(value = "/school/{schoolid}/grades")
	public List<GradeResponse> listGrade(@PathVariable("schoolid") String schoolid) {
		List<GradeResponse> rList = new ArrayList<>();
		
		SchoolStudyStageEntity ex = new SchoolStudyStageEntity();
		ex.setSchoolid(schoolid);
		List<SchoolStudyStageEntity> list = stageService.findAll(Example.of(ex));
		if(!list.isEmpty()) {
			List<StudyStage> sList = list.stream().map(SchoolStudyStageEntity::getState).collect(Collectors.toList());
			List<GlobalGradeStage> gsList =  sList.stream().flatMap(e->{
				return GlobalGradeStage.getGradeStages(e).stream();
			}).collect(Collectors.toList());
			
			for (GlobalGradeStage e : gsList) {
				GradeResponse r = new GradeResponse();
				r.setName(e.name());
				r.setTitle(e.getTitle());
				rList.add(r);
			}
		}
		
		return rList;
	}
	
	
	
	
}
