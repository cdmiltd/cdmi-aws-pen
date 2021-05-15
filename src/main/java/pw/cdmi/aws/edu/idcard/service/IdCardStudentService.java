package pw.cdmi.aws.edu.idcard.service;

import java.util.List;

import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;

public interface IdCardStudentService extends BaseService<IdCardStudentEntity,String>{
	
	
	public int deleteByCardIds(List<String> ids);
	
	public void batchSave(List<IdCardStudentEntity> list);
	
	/**
	 * 根据点位查询 学生
	 * @param cardId   批改版 id
	 * @param x		   x	 
	 * @param y        
	 * @return
	 */
	public IdCardStudentEntity selectStudentByRange(String cardId,float x,float y);
}
