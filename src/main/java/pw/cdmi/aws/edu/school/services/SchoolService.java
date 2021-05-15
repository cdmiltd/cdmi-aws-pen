package pw.cdmi.aws.edu.school.services;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;

public interface SchoolService extends BaseService<SchoolEntity, String>{
	
	/**
	 * 添加学校学部 小学 初中 高中
	 */
	public void createsSchool(String[] stages,SchoolEntity school);
	
	
	
	public void updateSchool(String[] stages,SchoolEntity school);
	
	/**
	 * 添加管理员
	 * @param school
	 * @param name
	 * @param phone
	 */
	public void addManage(String schoolId,String name,String phone);
	
	SchoolEntity getByManageId(String manageId);

}
