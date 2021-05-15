package pw.cdmi.aws.edu.pdf.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

import net.coobird.thumbnailator.Thumbnails;

public class PDFPage2Image {

	public static void pdfpage2image() throws IOException {
		PDDocument pd = PDDocument.load(new File("/Users/liwenping/Downloads/pdf.pdf"));
		System.out.println(pd.getNumberOfPages());
		PDFRenderer r = new PDFRenderer(pd);
		BufferedImage tmp = r.renderImageWithDPI(0, 300f); // 获取指定页数转换为图片
		
		
	
		
//		PDRectangle box = pd.getPage(Integer.valueOf(String.valueOf(0))).getBBox();
//		
//		System.out.println(new BigDecimal(String.valueOf(box.getHeight())).multiply(new BigDecimal("25.4")).divide(new BigDecimal("72"),4,RoundingMode.UP));
//		System.out.println(new BigDecimal(String.valueOf(box.getWidth())).multiply(new BigDecimal("25.4")).divide(new BigDecimal("72"),4,RoundingMode.UP));
//		System.out.println();
//		System.out.println((box.getHeight()*25.4/72));
//		System.out.println((box.getWidth()*25.4/72));
//		int width = (int) (((box.getWidth()*25.4/72) - 6) /25.4*300);
//		int height = (int) (((box.getHeight()*25.4/72) - 6) /25.4*300);
//		System.out.println(width);
//		System.out.println(height);
		
		

//		 
//		
//		
//	
//		Graphics2D g = bf.createGraphics();     //画笔 
//		g.drawImage(oimg, 0, 0, 1000, 700, null);  
//		Font font = new Font("宋体", Font.PLAIN, 27);
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //文字抗锯齿
//		g.setColor(Color.red);
//		g.setFont(font);
//		
//		g.drawString("aaaa", 20, 20);  //写文字
//
//		g.setColor(Color.black);
//		g.setFont(font);
//		g.drawString("sdsdssd我d", 50, 50);   
//
//		ImageIcon icon = new ImageIcon("d://t.png");
//		Image image = icon.getImage();
////		  BufferedImage image = ImageIO.read(new File("d://t.png"));
//		g.drawImage(image, 100, 100, 100, 100, null);   //写图片
//
//		g.dispose();
//		
////		g.setComposite(AlphaComposite.Src);
////		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
////		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
////		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//		 
//		
//		 //写图片
	
		ImageIO.write(tmp, "png", new File("/Users/liwenping/a3.png"));
		
//
		pd.close();
	}

	public static void main(String[] args) throws IOException {

		pdfpage2image();

	}
}
