package pw.cdmi.aws.edu.book.services.impl;

import java.util.List;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;
import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;
import pw.cdmi.aws.edu.book.repo.PDFPageRepository;
import pw.cdmi.aws.edu.book.services.PDFPageService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.impl.BaseServiceImpl;
import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;
import pw.cdmi.aws.edu.idcard.service.IdCardService;
import pw.cdmi.aws.edu.idcard.service.IdCardStudentService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.repo.SchoolClassTeamRepository;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

@Service
public class PDFPageServiceImpl extends BaseServiceImpl<PDFPageEntity, String> implements PDFPageService{

	
	@Autowired
	private PDFPageRepository repo;
	
	
	@Autowired
	private SchoolClassTeamRepository classTeamRepo;
	
	
	@Autowired
	private IdCardService idcardService;
	
	@Autowired
	private IdCardStudentService cardStudentService;
	
	/**
	 * 更具编码后的 pdf 创建 page id 信息
	 */
	@Override
	@Transactional
	public void createPages(Long beginPageId, Long endPageId, String classTeamId,Long sn,String path) {
		//第一页是老师批改版
		PDFPageEntity teacherPage = new PDFPageEntity();
		teacherPage.setPageId(beginPageId);
		teacherPage.setType(PDFTypeEnum.TeacherIdCard);
		teacherPage.setSourceId(classTeamId);
		repo.save(teacherPage);
		
		
		//修改批改版 page id 记录
		IdCardEntity ide = idcardService.getBySn(sn);
		if(ide == null || !ide.getClassTeamId().equals(classTeamId)) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		}
		ide.setPageId(beginPageId);
		ide.setStartPageId(beginPageId);
		ide.setEndPageId(endPageId);
		ide.setPdfEncodePath(path);
		idcardService.saveAndFlush(ide);
		
		
	
	
		SchoolClassTeamEntity classTeam = classTeamRepo.getOne(classTeamId);
		classTeam.setEncodeIdCardPdfPath(path);
		classTeamRepo.saveAndFlush(classTeam);
		
		//获取当批次批改版下面绑定的学生
		IdCardStudentEntity ex = new IdCardStudentEntity();
		ex.setCardId(ide.getId());
		Sort sort = Sort.by(Direction.ASC,"studentSn");
		List<IdCardStudentEntity> cardStudentList = cardStudentService.findAll(Example.of(ex), sort);
		
	
		
		
		
		
		long[] pageIds = LongStream.rangeClosed(beginPageId + 1, endPageId).toArray();
		
		for (int i = 0; i < pageIds.length; i++) {
			//创建学生的 page id 信息
			if(i < cardStudentList.size()) {
				PDFPageEntity s = new PDFPageEntity();
				s.setPageId(pageIds[i]);
				s.setType(PDFTypeEnum.StudentIdCard);
				s.setSourceId(cardStudentList.get(i).getStudentId());
				repo.save(s);
				
			}else {
//				创建空 page id
				PDFPageEntity empty = new PDFPageEntity();
				empty.setPageId(pageIds[i]);
				empty.setType(PDFTypeEnum.NULL);
				repo.save(empty);
			}
		}
		

		
		
		
		
		
	}

	@Override
	public PDFPageEntity getByPageId(Long pageId) {
		return repo.getByPageId(pageId);
	}

	@Override
	public void deleteSoucePageId(String souceId, PDFTypeEnum type) {
		repo.deleteSoucePageId(souceId, type);
	}

}
