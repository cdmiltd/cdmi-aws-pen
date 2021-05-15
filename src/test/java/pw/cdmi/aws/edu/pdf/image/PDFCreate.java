package pw.cdmi.aws.edu.pdf.image;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PDFCreate {
	public static void exportMapMZydPdf(String templatePath, String savePath, String fileName)
			throws DocumentException {
		ByteArrayOutputStream byteArrayOutputStream = null;

		PdfReader pdfReader = null;

		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(savePath + "//" + fileName);

			byteArrayOutputStream = new ByteArrayOutputStream();
//			ClassPathResource cpr = new ClassPathResource("static/idcard_pdf_template.pdf");
//			FileCopyUtils.copyToByteArray(cpr.getInputStream());
			
			pdfReader = new PdfReader("d://idcard_classteam_template_v2.pdf");

			PdfStamper pdfStamper = new PdfStamper(pdfReader, byteArrayOutputStream);
			
	
			BaseFont bf = 	BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			// 获取模板所有域参数

			AcroFields acroFields = pdfStamper.getAcroFields();
			
			

			// 解决中文字体不显示的问题

//			BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

//			ArrayList fontArrayList = new ArrayList();
//
////			fontArrayList.add(baseFont);
//
//			acroFields.setSubstitutionFonts(fontArrayList);

		
			acroFields.setFieldProperty("studentName", "textfont",bf , null);
		
			
			
			
			acroFields.setFieldProperty("classTeamName", "textfont",bf , null);
//			acroFields.setFieldProperty("t2", "textfont",bf , null);
//			acroFields.setFieldProperty("fill_1", "textfont",bf , null);
////			acroFields.setFieldProperty("t4", "textfont",bf , null);
////			acroFields.setFieldProperty("t5", "textfont",bf , null);
			acroFields.setField("classTeamName", "小学一年级01班");
			acroFields.setField("studentSn", "12");
			acroFields.setField("studentName", "王三会");
			acroFields.setField("schoolName", "成都金苹果幼儿园");
////			acroFields.setField("t2", "李四");
////			acroFields.setField("t3", "王五");
////			acroFields.setField("t4", "四个文字");
////			acroFields.setField("t5", "张三");
////			acroFields.setField("t6", "李四");
////			acroFields.setField("t7", "王五");
////			acroFields.setField("t8", "四个文字");
//			
//
//			acroFields.setField("fill_1", "张三李四");
//			acroFields.setField("fill_1", "学号:23");

			pdfStamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true

			pdfStamper.flush();

			pdfStamper.close();
			
			// 设置纸张，可以在Excel制作是设定好纸张大小

			Document doc = new Document(PageSize.A4);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfCopy copy = new PdfCopy(doc, baos);

			doc.open();

			PdfImportedPage importPage = copy.getImportedPage(new PdfReader(byteArrayOutputStream.toByteArray()),1);

		
			
			try {
				copy.addPage(importPage);
			

			} catch (BadPdfFormatException e) {
				e.printStackTrace();

			}

			doc.close();
			
			
			fileOutputStream.write(baos.toByteArray());
			fileOutputStream.flush();
			fileOutputStream.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();

				} catch (IOException e) {
					e.printStackTrace();

				}

			}

			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();

				} catch (IOException e) {
					e.printStackTrace();

				}

			}

			if (pdfReader != null) {
				pdfReader.close();

			}

		}

	}

	public static void main(String[] args) throws IOException {
		try {
			String savePath = "d:";

			String fileName = "2.pdf";

			exportMapMZydPdf("/Users/liwenping/idcard_pdf_template.pdf", savePath, fileName);

		} catch (DocumentException e) {
			e.printStackTrace();

		}

	}

}
