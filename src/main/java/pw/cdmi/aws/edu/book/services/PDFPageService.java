package pw.cdmi.aws.edu.book.services;

import pw.cdmi.aws.edu.book.modules.PDFTypeEnum;
import pw.cdmi.aws.edu.book.modules.entities.PDFPageEntity;
import pw.cdmi.aws.edu.common.service.BaseService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;

public interface PDFPageService extends BaseService<PDFPageEntity, String>{

	public PDFPageEntity getByPageId(Long pageId);
	
	public void createPages(Long beginPageId,Long endPageId,String classTeamId,Long sn,String path);
	
	public void deleteSoucePageId(String souceId,PDFTypeEnum type);
}
