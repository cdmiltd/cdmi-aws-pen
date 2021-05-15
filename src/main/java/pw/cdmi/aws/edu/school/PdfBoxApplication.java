package pw.cdmi.aws.edu.school;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class PdfBoxApplication {
    private static final String pdfFilePath = "D:\\demo2.pdf";

    public static void main(String[] args) {
        String result = null;
//        FileInputStream is = null;
        File file = new File(pdfFilePath);
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();

            result = stripper.getText(document);

            System.out.println("文档页数：" + document.getNumberOfPages());
            System.out.println("文档内容：");
            System.out.println(result);
            PDSignature signature = document.getLastSignatureDictionary();
            PDDocumentInformation info = document.getDocumentInformation();
            PDPageTree pageTree = document.getPages();
            PDPage page = pageTree.get(1);
            COSDictionary pageCOSObject = page.getCOSObject();
            Collection<COSBase> pageValues = pageCOSObject.getValues();
            List<PDAnnotation> annotations = page.getAnnotations();
            COSDictionary dictionary = pageTree.getCOSObject();
            Collection<COSBase> cosBases =  dictionary.getValues();
            System.out.println(result);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
