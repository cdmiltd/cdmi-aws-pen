package pw.cdmi.aws.edu.idcard.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.common.utils.RandomKeyUUID;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;
import pw.cdmi.aws.edu.idcard.repo.IdCardStudentRepository;
import pw.cdmi.aws.edu.idcard.service.IdCardStudentService;

@Service
public class IdCardStudentServiceImpl extends BaseServiceImpl<IdCardStudentEntity,String> implements IdCardStudentService{

	@Autowired
	private IdCardStudentRepository repo;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	


	@Override
	public int deleteByCardIds(List<String> ids) {
		return repo.deleteByCardIds(ids);
	}


	@Override
	public void batchSave(List<IdCardStudentEntity> list) {
		jdbcTemplate.execute(buildBatchInsertSQL(list));
	}
	

	private String buildBatchInsertSQL(List<IdCardStudentEntity> list) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n"
				+ "insert  into edu_id_card_student (cardId, indexNum, studentCardId, studentId, studentSn,lowRightX,lowRightY,upleftX,upleftY, id)  values");
		list.forEach(e->{
			sb.append(String.format("('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'),", e.getCardId(),e.getIndexNum(),e.getStudentCardSn(),e.getStudentId(),e.getStudentSn(),e.getLowRightX(),e.getLowRightY(),e.getUpleftX(),e.getUpleftY(),StringUtils.isBlank(e.getId())? RandomKeyUUID.getUUID() : e.getId()));
		});
		
		return sb.toString().substring(0, sb.length()-1);
		
	}


	@Override
	public IdCardStudentEntity selectStudentByRange(String cardId, float x, float y) {
		return repo.selectStudentByRange(cardId, x, y);
	}
	
}
