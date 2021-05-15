package pw.cdmi.aws.edu.book.rs;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
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

import com.obs.services.model.PutObjectResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import pw.cdmi.aws.edu.book.modules.TextbookFormat;
import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.book.rs.request.BookRequest;
import pw.cdmi.aws.edu.book.rs.request.DeleteBookRequest;
import pw.cdmi.aws.edu.book.services.TextBookService;
import pw.cdmi.aws.edu.common.component.HuaWeiCloudOBSComponent;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.utils.PropUtil;
import pw.cdmi.aws.edu.idcard.entity.IdCardEntity;
import pw.cdmi.aws.edu.idcard.service.IdCardService;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.Semester;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.entities.SchoolBookEntity;
import pw.cdmi.aws.edu.school.services.SchoolBookService;
import pw.cdmi.core.exception.HttpClientException;
import pw.cdmi.utils.UUIDUtils;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu", description = "教育模块")
public class TextBookResource {
	
	 private static final Logger log = LoggerFactory.getLogger(TextBookResource.class);
    
    @Autowired
    private TextBookService bookService;

    @Value("${cdmi.aws.appId}")
    private String appId;
    
    @Value("${upload.local.path}")
    private String pdfLocalPath;
    
    @Value("${huawei.obs.public.bucketName}")
	private String publicBucket;
    
	@Value("${huawei.obs.north.endPoint}")
	private String endPoint;
    
    @Autowired
    private HuaWeiCloudOBSComponent uploadComponent;
    
    @Autowired
    private IdCardService idCardService;
    
    public Float dpi = 300f;
    
    @Autowired
    private SchoolBookService sbs;

    /**
     * 创建一个教辅材料
     * @param newbook
     * @return
     */
     @PostMapping("/book")
     public String postBook(@RequestBody @Validated BookRequest newbook) {
     	log.info("req newbook => [{}]",newbook);
         TextbookEntity entity = new TextbookEntity();
         BeanUtils.copyProperties(newbook, entity);
         if(newbook.getFormat() == null)
        	 entity.setFormat(TextbookFormat.BOOK);
         if(StringUtils.isNotBlank(entity.getImage())) {
 			StringBuffer sb = new StringBuffer();
 			sb.append("https://").append(publicBucket).append(".").append(endPoint).append("/").append(entity.getImage());
 			entity.setImage(sb.toString());
 		 }
         if(newbook.getStartPageno() != null) {
        	TextbookEntity check1 = bookService.selectBookByPageno(newbook.getStartPageno());
         	if(check1 != null) {
         		throw new HttpClientException(ErrorMessages.PageIdExistsException);
         	}
         }
         if(newbook.getEndPageno() != null) {
        	TextbookEntity check2 = bookService.selectBookByPageno(newbook.getEndPageno());
          	if(check2 != null) {
          		throw new HttpClientException(ErrorMessages.PageIdExistsException);
          	}
         }
         
         entity.setCreateDate(new Date());
         bookService.save(entity);
         return entity.getId();
     }
     
     
     /**
      * 为指定教材设置封面图片
      * @param bookid
      */
     @PostMapping("/book/image/{bookid}")
     public void putBookImage(@PathVariable("bookid") String bookid,@RequestParam("file") MultipartFile file) {
    	 if(file.isEmpty()){
             throw new HttpClientException(ErrorMessages.FileEmpty);
         }
    	TextbookEntity book = bookService.getOne(bookid);
        if(book == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String objectKey = UUIDUtils.getValueAfterMD5()+suffixName;
        
        PutObjectResult res;
		try {
			res = uploadComponent.uploadPublicBucket(objectKey,file.getInputStream());
			if(log.isDebugEnabled()) log.debug("originalName:[{}],uploadResult=>[{}]",fileName,res);
	    	if(res.getStatusCode() == 200) {
	    		book.setImage(res.getObjectUrl());
	    		bookService.saveAndFlush(book);
	    	}
		} catch (IOException e) {
			e.printStackTrace();
			 throw new HttpClientException(ErrorMessages.FileUploadError);
		}
		
		
    	
     }
     
     /**
      * 编辑指定教辅材料
      * @param bookid
      */
     @PutMapping("/book/{bookid}")
     public void putBook(@PathVariable("bookid") String bookid,@RequestBody  BookRequest req) {
     	TextbookEntity book = bookService.getOne(bookid);
     	if(book == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
     	
     	 req.setFormat(null);
         BeanUtils.copyProperties(req, book,PropUtil.getNullPropNames(req));
        
         if(StringUtils.isNotBlank(req.getImage())) {
        	 if(!req.getImage().startsWith("http")) {
	  			StringBuffer sb = new StringBuffer();
	  			sb.append("https://").append(publicBucket).append(".").append(endPoint).append("/").append(req.getImage());
	  			book.setImage(sb.toString());
         	}
  		 }
          log.info("entity=>{}",book);
          bookService.saveAndFlush(book);
         
     }
     
     /**
      * 为指定教材/试卷设置标引信息
      * @param bookid
      */
     @PutMapping("/book/{bookid}/pageinfo")
     public void putBookDetail(@PathVariable("bookid") String bookid,@RequestBody BookRequest req) {
    	 
     	if(req.getPageSize() == null) throw new HttpClientException(ErrorMessages.ArgValidException);
     	if(req.getStartPageno() == null) throw new HttpClientException(ErrorMessages.ArgValidException,"开始页号不能为空!");
     	if(req.getEndPageno() == null) throw new HttpClientException(ErrorMessages.ArgValidException,"结束页号不能为空!");
     	TextbookEntity book = bookService.getOne(bookid);
     	if(book == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
     	
     	
    	TextbookEntity check1 = bookService.selectBookByPageno(req.getStartPageno());
    	if(check1 != null && !check1.getId().equals(book.getId())) {
    		throw new HttpClientException(ErrorMessages.PageIdExistsException);
    	}
    	TextbookEntity check2 = bookService.selectBookByPageno(req.getEndPageno());
    	if(check2 != null && !check2.getId().equals(book.getId())) {
    		throw new HttpClientException(ErrorMessages.PageIdExistsException);
    	}
     	
     	book.setPageSize(req.getPageSize());
     	book.setStartPageno(req.getStartPageno());
     	book.setEndPageno(req.getEndPageno());
  
     	bookService.setBookPageIdInfo(book);
     }
   
     /**
      *  获取当前系统内的教辅材料列表
      * @param format
      * @param stage
      * @param course
      * @param grade
      * @param semester
      * @return
      */
    @GetMapping("/book")
    public List<TextbookEntity> listBook(
            @RequestParam(name="stage",required = false) StudyStage stage,
            @RequestParam(name="course",required = false) DefaultCourse course,
            @RequestParam(name="grade",required = false) GlobalGradeStage grade,
            @RequestParam(name="semester",required = false) Semester semester,
            @RequestParam(name="format",required = false,defaultValue = "BOOK") TextbookFormat format) {
    	
    	TextbookEntity e = new TextbookEntity();
    	e.setFormat(format);
    	if(course != null) {
    		e.setCourse(course);
    	}
    	if(stage != null) {
    		e.setStage(stage);
    	}
    	if(grade != null) {
    		e.setGrade(grade);
    	}
    	if(semester != null) {
    		e.setSemester(semester);
    	}
    	
		Example<TextbookEntity> example = Example.of(e);
		

        return  bookService.findAll(example);
    }

   
    /**
     * 获取指定教辅材料的详细信息
     * @param bookid
     */
    @GetMapping("/book/{bookid}")
    public TextbookEntity getBookDetail(@PathVariable("bookid") String bookid) {
    	TextbookEntity e =	bookService.getOne(bookid);
    	if(e!= null && StringUtils.isNotBlank(e.getTestBookId()) && e.getFormat() == TextbookFormat.BOOK) {
    		e.setTestBook(bookService.getOne(e.getTestBookId()));
    	}
        return e;
    }
    
    
    
    
    /**
     * 下载pdf页
     * @param bookid
     */
    @GetMapping("/paper/{pageid}/image")
    public void downloadPaper(@PathVariable("pageid") Long pageid,HttpServletResponse response) {
    	
    	TextbookEntity book = bookService.selectBookByPageno(pageid);
    	log.info("pageid=>[{}],book=[{}]",pageid,book);
    	
    	if(book == null) {
    		IdCardEntity idCard = idCardService.getByPageId(pageid);
    		if(idCard != null) {
    			ClassPathResource idCardPng = new ClassPathResource("static/idcard.png");
    			
    			byte[] fileBytes;
				try {
					fileBytes = FileCopyUtils.copyToByteArray(idCardPng.getInputStream());
					outImage(response, fileBytes , "idcard");
				} catch (IOException e) {
					e.printStackTrace();
					throw new HttpClientException(ErrorMessages.BookPDFPageNoOverflow);
				};
				
    		}else {
    			throw new HttpClientException(ErrorMessages.BookPDFPageNoOverflow);
    		}
    		
    		
    	}
    	
    	String pdfPath = pdfLocalPath + File.separator + book.getPdfUrl();
    	
    	log.info("pdfPath=>[{}]",pdfPath);
    	
    	long pageIndex = pageid - book.getStartPageno();
    	if(pageIndex < 0) pageIndex = 0;
    	long maxIndex = book.getEndPageno() - book.getStartPageno();
    	if(pageIndex > maxIndex)  pageIndex = maxIndex;
    	
    	try {
    		File pdf = new File(pdfPath);
    		if(!pdf.exists()) throw new HttpClientException(ErrorMessages.FileNotFound);
			PDDocument pd = PDDocument.load(pdf);
			int pageNumber = pd.getNumberOfPages() - 1;
			log.info("总页数=>[{}],抓取页号=>[{}],dpi=>[{}]",pageNumber,pageIndex,dpi);
			if(pageIndex > pageNumber) pageIndex = pageNumber;
			
			PDFRenderer r = new PDFRenderer(pd);
			
			BufferedImage image = r.renderImageWithDPI(Integer.valueOf(String.valueOf(pageIndex)), dpi); //295.3 获取指定页数转换为图片
			
			
//			PDRectangle box = pd.getPage(Integer.valueOf(String.valueOf(pageIndex))).getBBox();
//			int width = (int) (((box.getWidth()*25.4/72) - mm) /25.4*300);
//			int height = (int) (((box.getHeight()*25.4/72) - mm) /25.4*300);
//			
//			log.info("剪裁 mm =>[{}]  width=>[{}],height=>[{}]",mm,width,height);
//			
//			BufferedImage finalImage = Thumbnails.of(image).size(width, height).asBufferedImage();
			
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image, "png",out);
			byte[] fileBytes = out.toByteArray();
			
			response.reset();
           
			outImage(response, fileBytes, String.valueOf(pageNumber));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return;
    }
    
    private void outImage(HttpServletResponse response,byte[] fileBytes,String pageIndex) throws IOException {
    	 // 设置response的Header
		response.setContentType("application/x-download;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=pdf_"+pageIndex+".png");
        response.addHeader("Content-Length", "" + fileBytes.length);
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(fileBytes);
        toClient.flush();
        toClient.close();
    }
    
    @PostMapping("/book/paper/size")
    public Float updateDpi(Float dpi) {
    	this.dpi = dpi;
    	return dpi;
    }

    /**
     * 删除一本没有引用的教辅材料
     * @param bookid
     */
    @DeleteMapping("/book")
    public void deleteBook(@RequestBody DeleteBookRequest req) {
    	if(StringUtils.isBlank(req.getBookId()) || StringUtils.isBlank(req.getBookName())) {
    		throw new HttpClientException(ErrorMessages.MissRequiredParameter);
    	}
    	TextbookEntity book = bookService.getOne(req.getBookId());
    	if(book == null || !req.getBookName().trim().equalsIgnoreCase(book.getName())) {
    		throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
    	}
    	
    	SchoolBookEntity ex = new SchoolBookEntity();
    	ex.setBookId(req.getBookId());
    	
    	//如果书籍没有分配到学校
    	long count = sbs.count(Example.of(ex));
    	if(count == 0) {
    		bookService.deleteBook(book);
    	}else {
    		throw new HttpClientException(ErrorMessages.DeleteDataException);
    	}
    		
    }

   
    
    
    /**
     * 为指定学校分配教辅材料
     * @param enable
     */
    @PutMapping("school/{schoolid}/book/{bookid}")
    public void bookbindSchool(@PathVariable("schoolid") String schoolid,@PathVariable("bookid") String bookid,@RequestParam(name="enable",defaultValue = "true") Boolean enable) {
    	
    	SchoolBookEntity entity = new SchoolBookEntity();
    	entity.setBookId(bookid);
    	entity.setSchoolId(schoolid);
    	
    	if(enable) {
    		SchoolBookEntity dbEntity = sbs.findOne(Example.of(entity));
        	if(dbEntity == null) {
        		entity.setEnable(enable);
        		sbs.save(entity);
        	}
    	}else {
    		SchoolBookEntity dbEntity = sbs.findOne(Example.of(entity));
    		if(dbEntity != null) {
    			sbs.delete(dbEntity);
    		}
    	}
    	
    	
    	
    }
    
    /**
     * 获取指定学校已分配教材列表
     * 获取指定学校、年级、学期的教辅材列表
     * @param schoolid
     * @return
     */
    @GetMapping("school/{schoolid}/book")
    public List<TextbookEntity> getSchoolBooks(@PathVariable("schoolid") String schoolid,
    		@RequestParam(name="grade",required = false) String grade,
    		@RequestParam(name="semester",required = false) String semester,
    		@RequestParam(name="course",required = false) String course,
    		  @RequestParam(name="stage",required = false) String stage){
    	List<TextbookEntity> data = new ArrayList<>();
    	SchoolBookEntity ex = new SchoolBookEntity();
    	ex.setSchoolId(schoolid);
		List<SchoolBookEntity> list = sbs.findAll(Example.of(ex));
		List<String> ids = list.stream().map(SchoolBookEntity::getBookId).collect(Collectors.toList());
		
		
//		TextbookEntity ex2 = new TextbookEntity();
//		if(StringUtils.isNotBlank(semester)) {
//			ex2.setSemester(Semester.valueOf(semester));
//		}
//		if(StringUtils.isNotBlank(grade)) {
//			ex2.setGrade(GlobalGradeStage.valueOf(grade));
//		}
		if(!ids.isEmpty()) {
			data = bookService.selectSchoolBook(ids, semester, grade,course,stage);
		}
			
    	return data;
    }
    
    
   
}
