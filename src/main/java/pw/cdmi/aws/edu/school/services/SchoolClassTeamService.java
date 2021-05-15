package pw.cdmi.aws.edu.school.services;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;

public interface SchoolClassTeamService extends BaseService<SchoolClassTeamEntity, String>{

	public int updateStudentNum(String classteamid);
}
