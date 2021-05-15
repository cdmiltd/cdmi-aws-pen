package pw.cdmi.aws.micro.s3.rc;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.obs.services.model.PutObjectResult;

import pw.cdmi.aws.edu.book.modules.entities.TextbookEntity;
import pw.cdmi.aws.edu.common.component.HuaWeiCloudOBSComponent;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.micro.s3.rc.models.UploadResponse;
import pw.cdmi.core.exception.HttpClientException;
import pw.cdmi.utils.UUIDUtils;

@RestController
@RequestMapping("/s3/v1")
public class FileResource {
    private static final Logger log = LoggerFactory.getLogger(FileResource.class);
    
    @Autowired
    private HuaWeiCloudOBSComponent uploadComponent;
    
    @Value("${upload.local.path}")
    private String localPath;
    

    @PostMapping("/file")
    public String upload(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()){
            throw new HttpClientException(ErrorMessages.FileEmpty);
        }
        
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String objectKey = UUIDUtils.getValueAfterMD5()+suffixName;
        try {
        	
        	//本地保留一份
        	if(suffixName.contains("pdf")) {
	        	String localKey = localPath+File.separator+objectKey;
	        	file.transferTo(new File(localKey));
	        	PutObjectResult res = uploadComponent.uploadPublicBucket(objectKey, new File(localKey));
	        	if(log.isDebugEnabled()) log.debug("originalName:[{}],uploadResult=>[{}]",fileName,res);
	        	if(res.getStatusCode() == 200) {
	        		UploadResponse r = new UploadResponse();
	        		r.setOriginalFilename(fileName);
	        		r.setObjectKey(objectKey);
	        		r.setObjectUrl(res.getObjectUrl());
	        		log.info("upload result ={}",r);
	        		return objectKey;
	        	}
        	}else {
        		PutObjectResult res = uploadComponent.uploadPublicBucket(objectKey,file.getInputStream());
	        	if(log.isDebugEnabled()) log.debug("originalName:[{}],uploadResult=>[{}]",fileName,res);
	        	if(res.getStatusCode() == 200) {
	        		UploadResponse r = new UploadResponse();
	        		r.setOriginalFilename(fileName);
	        		r.setObjectKey(objectKey);
	        		r.setObjectUrl(res.getObjectUrl());
	        		log.info("upload result ={}",r);
	        		return objectKey;
	        	}
        	}
        	
        	
        	
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        
         throw new HttpClientException(ErrorMessages.FileUploadError);
    }

    @PostMapping("/files")
    public List<UploadResponse> handFileUpload(@RequestParam("files") MultipartFile[] files){
    	if(files == null || files.length == 0) {
    		throw new HttpClientException(ErrorMessages.FileEmpty);
    	}
    	List<UploadResponse> rlist = new ArrayList<UploadResponse>();
    	try {
    		for (MultipartFile file : files) {
        		if(!file.isEmpty()) {
        			String fileName = file.getOriginalFilename();
         	       	String suffixName = fileName.substring(fileName.lastIndexOf("."));
         	        String objectKey = UUIDUtils.getValueAfterMD5()+suffixName;
         	        
         	        
         	     //本地保留一份
                	if(suffixName.contains("pdf")) {
	                	String localKey = localPath+File.separator+objectKey;
	                	file.transferTo(new File(localKey));
	         	        
	         	        PutObjectResult res = uploadComponent.uploadPublicBucket(objectKey, new File(localKey));
	         	        if(log.isDebugEnabled()) log.debug("originalName:[{}],uploadResult=>[{}]",fileName,res);
	         	        if(res.getStatusCode() == 200) {
	         	        	UploadResponse r = new UploadResponse();
	                		r.setOriginalFilename(fileName);
	                		r.setObjectKey(objectKey);
	                		r.setObjectUrl(res.getObjectUrl());
	                		rlist.add(r);
	         	        }
                	}else {
                		 PutObjectResult res = uploadComponent.uploadPublicBucket(objectKey, file.getInputStream());
 	         	        if(log.isDebugEnabled()) log.debug("originalName:[{}],uploadResult=>[{}]",fileName,res);
 	         	        if(res.getStatusCode() == 200) {
 	         	        	UploadResponse r = new UploadResponse();
 	                		r.setOriginalFilename(fileName);
 	                		r.setObjectKey(objectKey);
 	                		r.setObjectUrl(res.getObjectUrl());
 	                		rlist.add(r);
 	         	        }
                	}
         	        
         	        
        		}
    		}
    		return rlist;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	throw new HttpClientException(ErrorMessages.FileUploadError);
    	
    }

    @GetMapping("/file/{fileId}")
    public String downloadFile(@RequestParam("prefix") String prefix,@PathVariable("fileId")String objectKey, HttpServletRequest request,
                               HttpServletResponse response) {
        String fileName = ""; //文件名
        if(fileName != null){
            File file = new File("G:\\upload\\dd.txt");
            if(file.exists()){
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition","attachment;fileName=" + fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try{
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer,0,i);
                        i = bis.read(buffer);
                    }
                    return "download success";
                }catch (FileNotFoundException ex){
                    ex.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try {
                        bis.close();
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "download failure";
    }
    
    @DeleteMapping("/file/{fileid}")
    public void deleteFile(@PathVariable("fileid") String fileid, @RequestParam("prefix") String prefix) {

    }
    
    /**
     * 下载pdf页
     * @param bookid
     */
    @GetMapping("/test")
    public void downloadPaper(String file,HttpServletResponse response) {
    	try {
    		File pdf = new File("/opt/upload/idcardpdf/"+file);
    	
			PDDocument pd = PDDocument.load(pdf);
		
			
			PDFRenderer r = new PDFRenderer(pd);
			
			BufferedImage image = r.renderImageWithDPI(0, 300); //295.3 获取指定页数转换为图片
			
			

			
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image, "png",out);
			byte[] fileBytes = out.toByteArray();
			
			response.reset();
            // 设置response的Header
			response.setContentType("application/x-download;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=pdf_1.png");
            response.addHeader("Content-Length", "" + fileBytes.length);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(fileBytes);
            toClient.flush();
            toClient.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return;
    }
}
