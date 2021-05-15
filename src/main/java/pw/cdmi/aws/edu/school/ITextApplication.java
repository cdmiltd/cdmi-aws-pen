package pw.cdmi.aws.edu.school;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PageRange;
import com.itextpdf.kernel.utils.PdfSplitter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ITextApplication {
    private static final String ORIG = "D:\\demo1.pdf";
    private static final String OUTPUT_FOLDER = "D:\\split";

    public static void main(String[] args) throws IOException {
        //1. 根据区域获取区域内文本
        //2. 获取PDF文本
        //3. 自动试题排版区域
        //4. 根据模块生成PDF文件
        //5. 向PDF文件写入试题排版信息
        //6. 从PDF文件中读出试题排版信息
        //7. 根据排版位置获取试题编号
        //打开文件
        //向文件中写入
        String pageContent = "AAA";
        Rectangle rect = new Rectangle(0, 100, 300, 700);
        RenderFilter filter = new RegionTextRenderFilter(rect);
        PdfReader reader1 = new PdfReader(ORIG);
        int pageNum = reader1.getNumberOfPages();
        TextExtractionStrategy strategy;
        for (int i = 1; i <= pageNum; i++) {
            strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
            pageContent +=PdfTextExtractor.getTextFromPage(reader1, i, strategy);
        }
        System.out.println(pageContent);
//        // Make sure to load license file before invoking any code
//        LicenseKey.loadLicenseFile(pathToLicenseFile);
//
//// Parse template into an object that will be used later on
//        Template template = Pdf2DataExtractor.parseTemplateFromPDF(pathToPdfTemplate);
//
//// Create an instance of Pdf2DataExtractor for the parsed template
//        Pdf2DataExtractor extractor = new Pdf2DataExtractor(template);
//
//// Feed file to be parsed against the template. Can be called multiple times for different files
//        ParsingResult result = extractor.recognize(pathToFileToParse);
//
//// Save result to XML or explore the ParsingResult object to fetch information programmatically
//        result.saveToXML(pathToOutXmlFile);
////        SplitPdf();
        Document document = new Document();
        com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(ORIG);
        String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);

//		System.out.println(textFromPage);
        AcroFields fields  = reader.getAcroFields();
        Map<String, TextField> map = fields.getFieldCache();
        PRAcroForm form = reader.getAcroForm();
        HashMap<Object, PdfObject> destions = reader.getNamedDestination();

        byte[] meta = reader.getMetadata();
        PRIndirectReference reference = reader.getPageOrigRef(1);
        PdfDictionary page = reader.getPageN(1);
        Set<PdfName> pageKeys = page.getKeys();
//		List<PRAcroForm.FieldInformation> form_fields =form.getFields();
//		for(Map.Entry<String, TextField> entry : map.entrySet()){
//			String mapKey = entry.getKey();
//			TextField field = entry.getValue();
//			System.out.println(field+":"+field.getDefaultText());
//		}
        //能获得文件标题，但与书本标识不同
        Map<String, String> info = reader.getInfo();
        //得到书本页数
        System.out.println(reader.getNumberOfPages());
        PdfDictionary dictionary = reader.getCatalog();
        Set<PdfName> keys = dictionary.getKeys();
        List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);

//		for (Iterator<HashMap<String, Object>> i = list.iterator (); i.hasNext () ; ) {
//
//			showBookmark ( i.next ()) ;
//
//		}
//		for ( Iterator<HashMap<String, Object>> i = list.iterator () ; i.hasNext () ; ) {
//
//			getPageNumbers( i.next ());
//		}
        reader.close();
//		DocumentPieceInfo dpi = new DocumentPieceInfo();
//		PdfStamper stamp = new PdfStamper(reader, pos);
    }

    //获取标题
    private static void showBookmark ( HashMap<String, Object> bookmark) {
        System.out.println (bookmark.get ( "Title" )) ;
        @SuppressWarnings("unchecked")
        ArrayList<HashMap<String, Object>> kids =  (ArrayList<HashMap<String, Object>>) bookmark.get ( "Kids" ) ;
        if ( kids == null )
            return ;
        for ( Iterator<HashMap<String, Object>> i = kids.iterator () ; i.hasNext () ; ) {

            showBookmark ( i.next ()) ;
        }
    }
    //获取页码
    public static void getPageNumbers(HashMap<String, Object> bookmark) {
        if (bookmark == null)
            return;

        if ("GoTo".equals(bookmark.get("Action"))) {

            String page = (String)bookmark.get("Page");
            if (page != null) {

                page = page.trim();

                int idx = page.indexOf(' ');

                int pageNum;

                if (idx < 0){

                    pageNum = Integer.parseInt(page);
                    System.out.println ("pageNum :"+ pageNum) ;
                }
                else{

                    pageNum = Integer.parseInt(page.substring(0, idx));
                    System.out.println ("pageNum:" +pageNum) ;
                }
            }
            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> kids =  (ArrayList<HashMap<String, Object>>) bookmark.get ( "Kids" ) ;
            if ( kids == null )
                return ;
            for ( Iterator<HashMap<String, Object>> i = kids.iterator () ; i.hasNext () ; ) {

                getPageNumbers ( i.next ()) ;
            }

        }
    }

    public static void SplitPdf() throws IOException {
        final int maxPageCount = 2;
        PdfDocument pdfDocument = new PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(new File(ORIG)));
        PdfSplitter pdfSplitter = new PdfSplitter(pdfDocument) {
            int partNumber = 1;

            @Override
            protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
                try {
                    return new PdfWriter(OUTPUT_FOLDER + "splitDocument_" + partNumber++ + ".pdf");
                } catch (final FileNotFoundException ignored) {
                    throw new RuntimeException();
                }
            }
        };

        pdfSplitter.splitByPageCount(maxPageCount, (pdfDoc, pageRange) -> pdfDoc.close());
        pdfDocument.close();
    }
}
