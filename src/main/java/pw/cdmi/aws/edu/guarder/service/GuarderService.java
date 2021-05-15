package pw.cdmi.aws.edu.guarder.service;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;
import pw.cdmi.aws.edu.guarder.rs.response.GuarderRequest;

import java.util.List;

public interface GuarderService extends BaseService<GuarderEntity, String>{

	/**
	 * 创建一个监护人
	 * @param studentid
	 * @param req
	 */
	public GuarderEntity createGuarder(String studentid,GuarderRequest req);
	
	
	/**
	 * 修改指定学生的监护人信息
	 * @param studentid
	 * @param req
	 */
	public void modifyGuarder(GuarderStudentEntity gse,GuarderEntity ge,GuarderRequest req);

	/**
	 * 根据手机号查询监护人信息
	 * @param phone
	 * @return
	 */
	List<GuarderEntity> findByPhone(String phone);
}
