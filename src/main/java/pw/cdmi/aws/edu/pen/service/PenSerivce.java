package pw.cdmi.aws.edu.pen.service;

import java.util.List;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.pen.modules.PenPoint;
import pw.cdmi.aws.edu.pen.modules.entities.PenEntities;

public interface PenSerivce extends BaseService<PenEntities, String>{

	
	public int importPen(List<PenEntities> data);
	
	
	/**
	 * 
	 * @param data
	 * @return 解析成功记录数,
	 */
	public int parseSyncPoints(String mac, List<PenPoint> data);

	/**
	 * 切换批改学生
	 * @param mac
	 * @param studentId
	 * @return
	 */
	void switchStudent(String mac, String studentId);
}

