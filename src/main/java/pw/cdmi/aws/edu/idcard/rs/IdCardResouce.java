package pw.cdmi.aws.edu.idcard.rs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.itextpdf.text.pdf.PdfReader;

import pw.cdmi.aws.edu.book.services.PDFPageService;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.idcard.entity.IdCardRecordEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;
import pw.cdmi.aws.edu.idcard.repo.IdCardRepository;
import pw.cdmi.aws.edu.idcard.rs.request.BindStudentRequest;
import pw.cdmi.aws.edu.idcard.rs.request.IdCardCreateRequest;
import pw.cdmi.aws.edu.idcard.rs.response.IDCardRecordResponse;
import pw.cdmi.aws.edu.idcard.rs.response.IdCardNewFiveResponse;
import pw.cdmi.aws.edu.idcard.rs.response.IdCardStatisticResponse;
import pw.cdmi.aws.edu.idcard.service.IdCardRecordService;
import pw.cdmi.aws.edu.idcard.service.IdCardService;
import pw.cdmi.aws.edu.idcard.service.IdCardStudentService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.services.SchoolClassTeamService;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1/idcard")
public class IdCardResouce {

	@Autowired
	private IdCardRecordService recordService;

	@Autowired
	private IdCardService cardService;

	@Autowired
	private IdCardStudentService cardStudentService;

	@Value("${system.manage.email}")
	private String email;

	private static final Logger log = LoggerFactory.getLogger(IdCardResouce.class);

	

	

	
	/**
	 * 批改版搜索
	 */
	
	@Autowired
	IdCardRepository r;
	@GetMapping("/findIdCardList")
	public List<IdCardNewFiveResponse>  findNewFive(@RequestParam(required = false) Long idCardSn,@RequestParam(name="size",required = false,defaultValue = "5") Integer size){
	
		List<Map<String,String>> list = r.findNewFive(idCardSn,size);
		
		List<IdCardNewFiveResponse> resp = JSON.parseObject(JSON.toJSONString(list),new TypeReference<List<IdCardNewFiveResponse>>() {});
		
		return resp;
	}
	

	/**
	 * 获取学生教材码统计信息
	 * 
	 * @return
	 */
	@GetMapping("/statistic")
	public IdCardStatisticResponse statistic() {
		IdCardStatisticResponse resp = new IdCardStatisticResponse();

		IdCardStudentEntity ex = new IdCardStudentEntity();

		long total = cardStudentService.count(Example.of(ex));

		ex.setStudentId("");
		long noBindTotal = cardStudentService.count(Example.of(ex));

		resp.setTotal(total);
		resp.setActivated(total - noBindTotal);

		return resp;
	}

	@Autowired
	private SchoolClassTeamService classTeamService;
	
	@Autowired
	private PDFPageService pdfPageService;

	@Value("${idcard.pdf.local.path}")
	private String pdfPath;

	/**
	 * 按照班级班级创建批改版
	 */
	@PostMapping("create/classteam")
	public boolean createByClassTeam(@RequestParam("classTeamId") String classTeamId) {
		
		return cardService.createByClassTeamId(classTeamId);
	}

	/**
	 * 上传编码后pdf 文件
	 * 
	 * @param classTeamId
	 */
	@PostMapping("/upload")
	public String upload(@RequestParam("classTeamId") String classTeamId,@RequestParam("sn")Long sn,@RequestParam("beginPageId") Long beginPageId,@RequestParam("endPageId") Long endPageId, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new HttpClientException(ErrorMessages.FileEmpty);
		}
		if(endPageId <= beginPageId) {
			throw new HttpClientException(ErrorMessages.ArgValidException);
		}
		try {
			PdfReader reader = new PdfReader(file.getInputStream());
	    	int num = reader.getNumberOfPages();//获得页数
	    	if((endPageId - beginPageId + 1) < num) {
	    		throw new RuntimeException("PDF页数和PageId范围不匹配");
	    	}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(pdfPageService.getByPageId(beginPageId) != null || pdfPageService.getByPageId(endPageId) != null) {
			throw new HttpClientException(ErrorMessages.PageIdExistsException);
		}
		
		SchoolClassTeamEntity classTeam = classTeamService.getOne(classTeamId);
		if (classTeam == null || classTeam.getIdCardSn() == null || classTeam.getIdCardSn().longValue() != sn) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		}
		
		String objectKey = "PGB-" + classTeamId + "-" + classTeam.getIdCardSn() + "-encode.pdf";

		String path = pdfPath + File.separator + objectKey;
		

		try {
			file.transferTo(new File(path));
			
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		//创建 page 信息  需要批改版的批次号(以免上传编码文件时 又重新生成新的 班级批改文件)
		pdfPageService.createPages(beginPageId, endPageId, classTeam.getId(),sn,path);
	
		return objectKey;
	}

	/**
	 * 老师下载铺码后的 pdf 文件
	 * 
	 * @param classTeamId
	 */
	@GetMapping("/download")
	public void download(@RequestParam("classTeamId") String classTeamId, HttpServletResponse response) {
		SchoolClassTeamEntity classTeam = classTeamService.getOne(classTeamId);
		if (classTeam == null) {
			throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
		}
		if (classTeam.getIdCardSn() == null) {
			throw new HttpClientException(ErrorMessages.NotUploadEncodedIdCardPDF);
		}
		String pdfPath = classTeam.getEncodeIdCardPdfPath();
		if (StringUtils.isBlank(pdfPath)) {
			throw new HttpClientException(ErrorMessages.NotUploadEncodedIdCardPDF);
		}
	
		String classTeamName = DateUtil.getGrade(classTeam.getEndYear(), classTeam.getStage()).getTitle()
				+ classTeam.getOrderValue() + "班";

		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(pdfPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			String fileName = "PGB-" + classTeamName + "-" + classTeam.getIdCardSn() + ".pdf";
			String formFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			// 设置response的Header
			response.setContentType("application/x-download;charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + formFileName);
			response.addHeader("Content-Length", "" + buffer.length);
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return;

	}

}
