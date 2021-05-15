package pw.cdmi.aws.edu.idcard.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardRecordEntity;
import pw.cdmi.aws.edu.idcard.entity.IdCardStudentEntity;
import pw.cdmi.aws.edu.idcard.service.IdCardRecordService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.services.SchoolService;

/**
 * 批改版异步处理
 * 
 * @author Administrator
 *
 */
@Service
public class IdCardAsyncService {

	private final Logger log = LoggerFactory.getLogger(IdCardAsyncService.class);



	
	
	

	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private IdCardRecordService recordService;
	
	@Autowired
	private SchoolService schoolService;


	

	
	
	@Async
	@Transactional
	public void createByClassTeamId(SchoolClassTeamEntity classTeam,List<StudentEntity> studentList,String path) {
		//首页生成教师批改版包含所有学生
		String classTeamName = DateUtil.getGrade(classTeam.getEndYear(), classTeam.getStage()).getTitle()+classTeam.getOrderValue()+"班";
		SchoolEntity school = schoolService.getOne(classTeam.getSchoolId());
		try {
			
			Document doc = new Document(PageSize.A4);
			
			PdfCopy copy = new PdfCopy(doc, new FileOutputStream(new File(path)));
			doc.open();
			//生成教师的批改班
			copy.addPage(copy.getImportedPage(getTeacherIdCardPdfReader(studentList,classTeam.getIdCardSn(),classTeamName,school.getName()), 1));
			
			//每个学生生成一页
			for (StudentEntity e : studentList) {
				copy.addPage(copy.getImportedPage(getStudentIdCardPdfReader(e,classTeamName,school.getName()), 1));
			}
			
			doc.close();
			
			sendEmial(path,school.getName()+ "-"+classTeamName);
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	private PdfReader getTeacherIdCardPdfReader(List<StudentEntity> studentList,Long idCardSn,String classTeamName,String schoolName) throws IOException, DocumentException {
		ClassPathResource cpr = new ClassPathResource("static/idcard_classteam_template_v2.pdf");
		byte[] bytes = FileCopyUtils.copyToByteArray(cpr.getInputStream());
		PdfReader pdfReader = new PdfReader(bytes);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
		// 获取模板所有域参数
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
				BaseFont.NOT_EMBEDDED);
		AcroFields acroFields = pdfStamper.getAcroFields();

		// 替换文本域参数
		acroFields.setFieldProperty("classTeamName", "textfont",bf , null);
		acroFields.setFieldProperty("schoolName", "textfont",bf , null);
		acroFields.setField("sn", String.valueOf(idCardSn));
		acroFields.setField("classTeamName", classTeamName);
		acroFields.setField("schoolName", schoolName);
		
		for (int i = 1; i <= 60; i++) {
			if(i <= studentList.size()) {
				acroFields.setFieldProperty("student"+studentList.get(i-1).getSn(), "textfont",bf , null);
				acroFields.setField("student"+studentList.get(i-1).getSn(),studentList.get(i-1).getName());
			}
		}
		

		pdfStamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true

		pdfStamper.flush();

		pdfStamper.close();

		return new PdfReader(baos.toByteArray());
	}
	
	private PdfReader getStudentIdCardPdfReader(StudentEntity student,String classTeamName,String schoolName) throws IOException, DocumentException {
		ClassPathResource cpr = new ClassPathResource("static/idcard_student_template_v2.pdf");
		byte[] bytes = FileCopyUtils.copyToByteArray(cpr.getInputStream());
		PdfReader pdfReader = new PdfReader(bytes);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
		// 获取模板所有域参数
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
				BaseFont.NOT_EMBEDDED);
		AcroFields acroFields = pdfStamper.getAcroFields();

		String studentSn = student.getSn() < 10 ? "0"+student.getSn() : student.getSn()+"";
		// 替换文本域参数
		acroFields.setFieldProperty("classTeamName", "textfont",bf , null);
		acroFields.setFieldProperty("schoolName", "textfont",bf , null);
		acroFields.setField("classTeamName", classTeamName);
		acroFields.setField("studentSn", studentSn);
		acroFields.setField("studentName", student.getName());
		acroFields.setField("schoolName", schoolName);
		
		acroFields.setFieldProperty("s1", "textfont",bf , null);
		acroFields.setField("s1", student.getName());
		acroFields.setFieldProperty("a1", "textfont",bf , null);
		acroFields.setField("a1","学号："+studentSn);
		

		pdfStamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true

		pdfStamper.flush();

		pdfStamper.close();

		return new PdfReader(baos.toByteArray());
	}
	
	
	@Value("${system.manage.email}")
	private String toEmail;

	public void sendEmial(String pdfFilePath,String name) {
		log.info("pdf文件生成成功发送邮件中");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		// 用MimeMessageHelper来包装MimeMessage
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject("纸云平台");
			mimeMessageHelper.setText("批改版PDF文件:"+name);
			mimeMessageHelper.setTo(toEmail);
			mimeMessageHelper.setFrom("18224431747@163.com");
			String fileName =  "PGB-"+name+ ".pdf";
			mimeMessageHelper.addAttachment(fileName,new File(pdfFilePath));
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		log.info("邮件发送成功");
	}

	
	
	

	

	

	
}
