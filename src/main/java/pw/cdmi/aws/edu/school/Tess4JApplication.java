package pw.cdmi.aws.edu.school;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.util.Map;

public class Tess4JApplication {
    public static void main(String[] args) {
        Map<String, String> map = System.getenv();
        String prefix = map.get("TESSDATA_PREFIX");
        System.out.println(prefix);
        File imageFile = new File("D:\\itcast\\env\\tess4j\\input\\text.png");
        System.out.println("文件是否存在:" + imageFile.exists());
        Tesseract tessreact = new Tesseract();
        //需要指定训练集 训练集到 https://github.com/tesseract-ocr/tessdata 下载。
        tessreact.setDatapath("D:\\itcast\\env\\tess4j\\tessdata");
        //注意  默认是英文识别，如果做中文识别，需要单独设置。
        tessreact.setLanguage("chi_sim");
        try {
            String result = tessreact.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
