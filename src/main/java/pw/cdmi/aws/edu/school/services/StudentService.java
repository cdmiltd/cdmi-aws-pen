package pw.cdmi.aws.edu.school.services;

import java.util.List;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.rs.requests.ExportStudentEntity;
import pw.cdmi.aws.edu.school.rs.requests.StudentRequest;

public interface StudentService extends BaseService<StudentEntity, String> {
	
	/**
	 * excel 导入学生
	 * @param classid
	 * @param data
	 */
	public int exportStudent(String classid,List<ExportStudentEntity> data);
	
	public String addStudent(StudentRequest req);
	
	public List<StudentEntity> findAllByClassteamid(String classteamid);
}
