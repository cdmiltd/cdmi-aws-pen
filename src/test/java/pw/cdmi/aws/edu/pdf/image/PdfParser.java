package pw.cdmi.aws.edu.pdf.image;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PdfParser {
	 public static void main( String[] args ) throws IOException
	    {
	    
	    	PdfReader reader = new PdfReader("d://demo1.pdf"); 

	        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
	      
	        
	        
	    	int num = reader.getNumberOfPages();//获得页数
	    	System.out.println(num);
	    	System.out.println(reader.getPageSize(1).getWidth());
	    	
//	    	//解析图片和文字等
//	    	parser.processContent(1, new RenderListener() {
//				
//				public void renderText(TextRenderInfo arg0) {
//					
//					System.out.println(	arg0.getText());
//					
//				}
//				
//				public void renderImage(ImageRenderInfo renderInfo) {
//					 PdfImageObject image0;
//				        try {
//				            image0 = renderInfo.getImage();
//				            byte[] imageByte = image0.getImageAsBytes();
//				            System.out.println(imageByte.length);
////				            FileOutputStream out = new FileOutputStream("d:\\"+UUID.randomUUID()+".jpg");
////			                out.write(imageByte);
////			                out.flush();
////			                out.close();
//
//				        }  catch (MalformedURLException e) {
//				            e.printStackTrace();
//				        } catch (IOException e) {
//				            e.printStackTrace();
//				        }
//					
//				}
//				
//				public void endTextBlock() {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				public void beginTextBlock() {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//	    	
	    }
}
