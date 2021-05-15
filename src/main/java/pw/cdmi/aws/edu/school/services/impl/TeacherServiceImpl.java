package pw.cdmi.aws.edu.school.services.impl;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.console.modules.UserRole;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.services.AppUserService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;
import pw.cdmi.aws.edu.school.repo.TeacherRepository;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.TeacherService;
import pw.cdmi.core.exception.HttpClientException;
import pw.cdmi.utils.MD5Utils;

@Service
public class TeacherServiceImpl extends BaseServiceImpl<TeacherEntity, String> implements TeacherService {

	
	@Autowired
	private TeacherRepository repo;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private AppUserService userService;
	
	@Override
	public TeacherEntity getByPhone(String phone) {
		
		return repo.getByPhone(phone);
	}


	@Override
	public boolean  createTeacher(TeacherEntity entity) {
		AppUserEntity user = userService.getAppUserEntityByPhone(entity.getPhone());
		if(user == null) {
			user = new AppUserEntity();
			user.setTrueName(entity.getName());
			user.addRole(UserRole.Teacher);
			user.setPhone(entity.getPhone());
			try {
				user.setPassword(MD5Utils.getMD5("123456"));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			userService.save(user);
		}else {
			//手机号有管理员角色
			if(user.containsRole(UserRole.SchoolManager)) {
			   SchoolEntity school = schoolService.getByManageId(user.getId());
			   if(school != null && !school.getId().equals(entity.getSchoolId())) {
				   throw new HttpClientException(ErrorMessages.AddTeacherRoleException);
			   }
			}
			
			user.addRole(UserRole.Teacher);
			userService.saveAndFlush(user);
		}
		repo.save(entity);
	
		return true;
	}

	//移除 老电话的 教师角色
	public void removeOldPhoneTeacherRole(String oldPhone) {
		AppUserEntity oldUser = userService.getAppUserEntityByPhone(oldPhone); //原账号
		if(oldUser != null) {
			oldUser.removeRole(UserRole.Teacher);
			userService.saveAndFlush(oldUser);
		}
	
		
	}
	
	@Override
	public String updateTeacher(TeacherEntity old,String newPhone) {
		
		//如果电话不为空,并且电话和之前电话不一样
		if (StringUtils.isNotBlank(newPhone) && !old.getPhone().equals(newPhone)) {
			
			//如果 电话 已经被其他老师使用
			TeacherEntity tmp = teacherService.getByPhone(newPhone);
			if(tmp != null) throw new HttpClientException(ErrorMessages.ExistsDataException);
			//原账号移除 teacher 角色
			removeOldPhoneTeacherRole(old.getPhone());
			
			
			
			old.setPhone(newPhone);
			
			//用户表是否已经有此用户
			AppUserEntity user = userService.getAppUserEntityByPhone(newPhone);
			if(user == null) {
				user = new AppUserEntity();
				user.setTrueName(old.getName());
				user.addRole(UserRole.Teacher);
				user.setPhone(newPhone);
				try {
					user.setPassword(MD5Utils.getMD5("123456"));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				userService.save(user);
			}else {
				//手机号有管理员角色
				if(user.containsRole(UserRole.SchoolManager)) {
				   SchoolEntity school = schoolService.getByManageId(user.getId());
				   if(!school.getId().equals(old.getSchoolId())) {
					   throw new HttpClientException(ErrorMessages.AddTeacherRoleException);
				   }
				}
				
				user.addRole(UserRole.Teacher);
				userService.saveAndFlush(user);
			}
			
		}
		
		
		repo.saveAndFlush(old);
		
		return old.getId();
	}


	@Override
	@Transactional
	public void deleteTeacher(TeacherEntity teacher) {
		
		AppUserEntity user = userService.getAppUserEntityByPhone(teacher.getPhone());
		if(user != null) {
			user.removeRole(UserRole.Teacher);
			userService.saveAndFlush(user);
			repo.deleteById(teacher.getId());
		}
		
		
	}
   

  
}
