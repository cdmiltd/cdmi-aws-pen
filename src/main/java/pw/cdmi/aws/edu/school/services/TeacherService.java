package pw.cdmi.aws.edu.school.services;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;

public interface TeacherService extends BaseService<TeacherEntity, String>{


	 TeacherEntity getByPhone(String phone);
	 
	 boolean  createTeacher(TeacherEntity entity);
	 
	 
	 String updateTeacher(TeacherEntity entity,String newPhone);
	 
	 
	 void deleteTeacher(TeacherEntity teacher);
}
