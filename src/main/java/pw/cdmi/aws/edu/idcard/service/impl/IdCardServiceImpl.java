package pw.cdmi.aws.edu.idcard.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.common.utils.RandomKeyUUID;
import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardRecordEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;
import pw.cdmi.aws.edu.idcard.repo.IdCardRepository;
import pw.cdmi.aws.edu.idcard.rs.request.IdCardCreateRequest;
import pw.cdmi.aws.edu.idcard.service.IdCardRecordService;
import pw.cdmi.aws.edu.idcard.service.IdCardService;
import pw.cdmi.aws.edu.idcard.service.IdCardStudentService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

@Service
public class IdCardServiceImpl extends BaseServiceImpl<IdCardEntity, String> implements IdCardService {

	private final Logger log = LoggerFactory.getLogger(IdCardServiceImpl.class);


	/**
	 * pdf模板 数据
	 */
	
	@Value("${idcard.pdf.local.path}")
	private String pdfPath;
	
	@Autowired
	private IdCardRepository repo;


	@Autowired
	private IdCardStudentService cardStudentService;


	@Autowired
	private StudentService studentService;

	@Autowired
	IdCardAsyncService asyncService;

	@Autowired
	private SchoolClassTeamService classTeamService;
	
	@Autowired
	RedisRandomService redisRandomService;

	@Override
	@Transactional
	public boolean createByClassTeamId(String classTeamId) {
		
		SchoolClassTeamEntity classTeam = classTeamService.getOne(classTeamId);
		if(classTeam == null) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		}
		
//		//删除老数据
//		deleteOldData(classTeamId);
		
		// 班级所有学生
		List<StudentEntity> studentList = studentService.findAllByClassteamid(classTeamId);
		if(studentList.isEmpty()) {
			return false;
		}

		Long newSn = redisRandomService.getNextSn();
		
		
		//修改班级批改版编号
		classTeam.setIdCardSn(newSn);
		//重新生成就设置编码后的pdf地址为空
		classTeam.setEncodeIdCardPdfPath("");
		classTeamService.saveAndFlush(classTeam);
		
		
		String cardPdfPath = pdfPath + File.separator + "PGB-"+ classTeam.getId() +"-" + classTeam.getIdCardSn() + ".pdf";
		
		//创建批改版
		IdCardEntity idCard = new IdCardEntity();
		idCard.setClassTeamId(classTeamId);
		idCard.setSn(newSn);
		idCard.setSchoolId(classTeam.getSchoolId());
		idCard.setCreateDate(new Date());
		idCard.setPdfPath(cardPdfPath);
		repo.save(idCard);

		//教师批改版学生绑定
		createIdCardStudent(studentList, idCard.getId());
		
		
		
		
		asyncService.createByClassTeamId(classTeam, studentList,cardPdfPath);
		return true;

	}
	
	
//	private void deleteOldData(String classTeamId) {
//		IdCardEntity old = repo.getByClassTeamId(classTeamId);
//		if(old != null) {
//			repo.delete(old);
//			cardStudentService.deleteByCardIds(Arrays.asList(old.getId()));
//		}
//	}
	
	private void createIdCardStudent(List<StudentEntity> studentList,String cardId) {
		int indexNum = 1;
		for (StudentEntity s : studentList) {
			IdCardStudentEntity cs = new IdCardStudentEntity();
			cs.setIndexNum(indexNum++);
			cs.setStudentId(s.getId());
			cs.setStudentSn(s.getSn());
			cs.setStudentCardSn(s.getCardSn());
			cs.setCardId(cardId);
			cs.caclRangePoint();
			cardStudentService.save(cs);
		}
		
	}
	
	

	

	

	@Override
	public Long selectMaxSn() {
		return repo.selectMaxSn();
	}

	
	
	@Override
	public IdCardEntity getByPageId(Long pageId) {
		return repo.getByPageId(pageId);
	}


	@Override
	public IdCardEntity getBySn(Long sn) {
		return repo.getBySn(sn);
	}

}
