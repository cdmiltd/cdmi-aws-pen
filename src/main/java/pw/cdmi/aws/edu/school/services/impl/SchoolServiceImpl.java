package pw.cdmi.aws.edu.school.services.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.console.modules.UserRole;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.repo.AppUserRepository;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.StudyTimeUnits;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolStudyStageEntity;
import pw.cdmi.aws.edu.school.repo.SchoolRepository;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.SchoolStudyStageService;
import pw.cdmi.core.exception.HttpClientException;
import pw.cdmi.utils.MD5Utils;

@Service
public class SchoolServiceImpl extends BaseServiceImpl<SchoolEntity, String> implements SchoolService {

	
	 private static final Logger log = LoggerFactory.getLogger(SchoolServiceImpl.class);
	
	@Autowired
	private SchoolRepository repo;

	@Autowired
	private SchoolStudyStageService stageRepo;

	@Autowired
	private AppUserRepository userRepo;

	@Override
	@Transactional
	public void createsSchool(String[] stages, SchoolEntity school) {
		String stageStr = StringUtils.join(stages, ",");
		log.info("set schoolid:[{}],stages:[{}]",school,stageStr);
		school.setStages(stageStr);
		school.setCreateDate(new Date());
		repo.save(school);

		// 创建学校所有的学部(小学 初中)

		for (int i = 0; i < stages.length; i++) {
			String stage = stages[i];
			StudyStage enu = StudyStage.fromName(stage);
			saveStudyState(enu,school.getId());
		}
		
	}
	

	// 创建学校学制列表 (小学/初中)
	private void saveStudyState(StudyStage s,String schoolid) {
		SchoolStudyStageEntity studyStageEntity = new SchoolStudyStageEntity();
		studyStageEntity.setState(s);
		studyStageEntity.setUnit(StudyTimeUnits.YEAR);
		studyStageEntity.setDuration(s.getYear());
		studyStageEntity.setSchoolid(schoolid);
		stageRepo.save(studyStageEntity);
	}
	


	@Override
	@Transactional
	public void addManage(String schoolId, String name, String phone) {
		
		SchoolEntity school = repo.getOne(schoolId);
		if(school == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		
		//移除当前学校管理员
		removeSchoolManage(school);
		
		AppUserEntity opt = userRepo.getByPhone(phone);
		if (opt == null) {
			AppUserEntity ue = new AppUserEntity();
			ue.setPhone(phone.trim());
			ue.setTrueName(name.trim());
			ue.addRole(UserRole.SchoolManager);
			try {
				ue.setPassword(MD5Utils.getMD5("123456"));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			userRepo.save(ue);
			school.setManageId(ue.getId());
		}else {
		   school.setManageId(opt.getId());
		   opt.addRole(UserRole.SchoolManager);
		   opt.setTrueName(name);
		   userRepo.saveAndFlush(opt);
		}
		
		repo.saveAndFlush(school);
	}
	
	
	/**
	 * 移除当前学校的管理员
	 * @param schoolId
	 */
	public void removeSchoolManage(SchoolEntity school) {
		if(StringUtils.isNotBlank(school.getManageId())) {
			//移除以前学校管理员的 学校管理员角色
			AppUserEntity entity =  userRepo.getOne(school.getManageId());
			if(entity != null) {
				entity.removeRole(UserRole.SchoolManager);
				userRepo.saveAndFlush(entity);
			}
		}
		school.setManageId("");
		//管理员id 设置为空
		repo.saveAndFlush(school);
		
	}


	@Override
	@Transactional
	public void updateSchool(String[] stages, SchoolEntity school) {
		
		repo.saveAndFlush(school);

		
		if(stages != null && stages.length > 0) {
			SchoolStudyStageEntity e = new SchoolStudyStageEntity();
			e.setSchoolid(school.getId());
			List<SchoolStudyStageEntity> stageList = stageRepo.findAll(Example.of(e));
			stageRepo.deleteInBatch(stageList);
			
			// 创建学校所有的学部(小学 初中)
			for (int i = 0; i < stages.length; i++) {
				String stage = stages[i];
				StudyStage enu = StudyStage.fromName(stage);
				saveStudyState(enu,school.getId());
			}
		}
		
	}


	@Override
	public SchoolEntity getByManageId(String manageId) {
		return repo.getByManageId(manageId);
	}

	

}
