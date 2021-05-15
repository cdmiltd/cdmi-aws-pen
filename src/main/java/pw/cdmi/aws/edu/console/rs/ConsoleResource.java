package pw.cdmi.aws.edu.console.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import pw.cdmi.aws.edu.common.cache.RedisUtil;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.service.SmnService;
import pw.cdmi.aws.edu.common.utils.DateUtil;
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.console.Exception.ConsoleErrorMessages;
import pw.cdmi.aws.edu.console.modules.LoginType;
import pw.cdmi.aws.edu.console.modules.UserRole;
import pw.cdmi.aws.edu.console.modules.entities.AppUserEntity;
import pw.cdmi.aws.edu.console.rs.request.BandingChildRequest;
import pw.cdmi.aws.edu.console.rs.response.UserToken;
import pw.cdmi.aws.edu.console.services.AppUserService;
import pw.cdmi.aws.edu.console.services.UserTokenHelper;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;
import pw.cdmi.aws.edu.guarder.service.GuarderService;
import pw.cdmi.aws.edu.guarder.service.GuarderStudentService;
import pw.cdmi.aws.edu.school.modules.entities.SchoolClassTeamEntity;
import pw.cdmi.aws.edu.school.modules.entities.SchoolEntity;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.modules.entities.TeacherEntity;
import pw.cdmi.aws.edu.school.rs.responses.SchoolDetaiResponse;
import pw.cdmi.aws.edu.school.rs.responses.StudentRowResponse;
import pw.cdmi.aws.edu.school.rs.responses.TeacherRowResponse;
import pw.cdmi.aws.edu.school.services.ClassTeamService;
import pw.cdmi.aws.edu.school.services.SchoolService;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.aws.edu.school.services.TeacherService;
import pw.cdmi.core.exception.HttpClientException;
import pw.cdmi.core.exception.HttpServiceException;
import pw.cdmi.utils.MD5Utils;

@RestController
@RequestMapping("/edu/v1/console")
@Tag(name = "edu", description = "教育模块")
public class ConsoleResource {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleResource.class);

    @Autowired
    private UserTokenHelper userTokenHelper;

    //验证码缓存前缀
    private String CAPTCHA_PREFIX = "captcha/";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AppUserService appUserService;
    
    @Autowired
    private GuarderStudentService gss;
    
    @Autowired
    private GuarderService gs;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ClassTeamService classTeamService;
    
    @Autowired
    private TeacherService teacherServie;

    /**
     * 获取用户手机验证码
     *
     * @param phone
     * @return 返回过期时间
     */
    @PutMapping("/mobile/code")
    public int putPhoneCode(@RequestParam(name = "phone") String phone, HttpServletRequest request) {
        if (StringUtils.isBlank(phone)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
        String code = SmnService.sendMessage(phone);
        int time = 10;
        redisUtil.set(CAPTCHA_PREFIX + phone, code, time * 60);
        return time;
    }

    /**
     * 通过手机验证码或用户密码进行登录
     *
     * @param phone
     * @param type
     * @param pwd
     * @
     */
    @PutMapping("/login")
    public UserToken loginSystemByPassword(@RequestParam(name = "phone") String phone,
                                           @RequestParam(name = "type") LoginType type, @RequestParam(name = "pwdcode") String pwd) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(pwd) || ObjectUtils.isEmpty(type)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }

        AppUserEntity appUserEntity = appUserService.getAppUserEntityByPhone(phone);
        if (ObjectUtils.isEmpty(appUserEntity)) {
            throw new HttpClientException(ConsoleErrorMessages.PhoneInvalidException);
        }

        if (LoginType.Code == type) {
            String cacheCaptcha = (String) redisUtil.get(CAPTCHA_PREFIX + phone);
            if (!pwd.equals(cacheCaptcha)) {
                throw new HttpClientException(ConsoleErrorMessages.CaptchaInvalidException);
            }
        } else {
            try {
                String pwdMd5 = MD5Utils.getMD5(pwd);
                if (!pwdMd5.equals(appUserEntity.getPassword())) {
                    throw new HttpClientException(ConsoleErrorMessages.PasswdInvalidException);
                }
            } catch (Exception e) {
                logger.error("md5 get fail, pwd:{}, message:{}", pwd, e.getMessage());
                throw new HttpServiceException(ErrorMessages.SystemException);
            }
        }

        UserToken userToken = userTokenHelper.getUserToken(appUserEntity);
        return userToken;
    }

    /**
     * 查询学校管理员管理的学校
     * @param userId
     * @return
     */
    @GetMapping("/user/manage/school")
    public List<SchoolDetaiResponse> schoolList(@RequestParam(name="userId",required = false)String userId){
    	if(StringUtils.isBlank(userId)) {
    		return new ArrayList<>();
    	}
    	AppUserEntity user = appUserService.getOne(userId);
    	if(user == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
    	SchoolEntity ex = new SchoolEntity();
    	ex.setManageId(userId);
    	List<SchoolEntity> list = schoolService.findAll(Example.of(ex)); 
    	return ListCopy.copyListProperties(list, SchoolDetaiResponse::new);
    }
    
    /**
     * 查询用户所属的教师信息
     * @param userId
     * @return
     */
    @GetMapping("/user/teacher/info")
    public TeacherRowResponse teacherInfo(@RequestParam(name="userId",required = false)String userId){
    	if(StringUtils.isBlank(userId)) {
    		return new TeacherRowResponse();
    	}
    	AppUserEntity user = appUserService.getOne(userId);
    	if(user == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
    	if(!user.getRoles().contains(UserRole.Teacher.name())) {
    		throw new HttpClientException(ErrorMessages.RoleException);
    	}
    	
    	TeacherEntity e = teacherServie.getByPhone(user.getPhone()); 
    	TeacherRowResponse res = new TeacherRowResponse();
    	if(e != null) {
    		org.springframework.beans.BeanUtils.copyProperties(e, res);
        	if(StringUtils.isNotBlank(e.getSchoolId())) {
    			SchoolDetaiResponse school = new SchoolDetaiResponse();
    			SchoolEntity entity = schoolService.getOne(e.getSchoolId());
    			if(entity != null) {
    				org.springframework.beans.BeanUtils.copyProperties(entity, school);
    				res.setSchool(school);
    			}
    		}
        	return res;
    	}
    	
    	
    	return null;
    }
    
    /**
     * 通过手机号码与验证码进行重设密码
     *
     * @param phone
     * @param code
     * @param newpwd
     */
    @PutMapping("/user/pwd")
    public void modifyUserPassword(@RequestParam(name = "code") String code
            , @RequestParam("newpwd") String newpwd, @RequestParam("phone") String phone) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code) || StringUtils.isBlank(newpwd)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }

        String cacheCaptcha = (String) redisUtil.get(CAPTCHA_PREFIX + phone);
        if (!code.equals(cacheCaptcha)) {
            throw new HttpClientException(ConsoleErrorMessages.CaptchaInvalidException);
        }

        AppUserEntity appUserEntity = appUserService.getAppUserEntityByPhone(phone);
        if (ObjectUtils.isEmpty(appUserEntity)) {
            throw new HttpClientException(ConsoleErrorMessages.PhoneInvalidException);
        }
        try {
            String newpwdMd5 = MD5Utils.getMD5(newpwd);
            appUserEntity.setPassword(newpwdMd5);
            appUserService.updateAppUserEntity(appUserEntity);
        } catch (Exception e) {
            logger.error("md5 get fail, newpwd:{}, message:{}", newpwd, e.getMessage());
            throw new HttpServiceException(ErrorMessages.SystemException);
        }
    }

    /**
     * 用户退出登录
     *
     * @param token
     */
    @PutMapping("/logout")
    public void logout(@RequestParam(name = "token") String token, HttpServletRequest request) {
        userTokenHelper.deleteUserToken(token);
    }

    /**
     * 用户切换登录身份
     *
     * @param role
     */
    @PutMapping("/user/{userId}/role")
    public void switchROle(@PathVariable String userId, @RequestParam(name = "role") String role, @RequestParam(name = "token") String token) {
        if (StringUtils.isBlank(token) || UserRole.fromName(role) == null) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        List<String> roleList = userToken.getRoles();
        for (String roleStr: roleList){
            if (roleStr.equals(role)){
                userToken.setCurrentRole(UserRole.fromName(role));
            }
        }
    }

    /**
     * 用户为家长时绑定自己的孩子
     *
     * @param userId
     */
    @PutMapping("/user/{userId}/child")
    public void bindingChild(@PathVariable("userId") String userId, @RequestBody BandingChildRequest req) {
        if (StringUtils.isBlank(userId)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
        //检查child信息
        if (StringUtils.isBlank(req.getName())
                || req.getSn() == null
                || StringUtils.isBlank(req.getSchoolId())
                || StringUtils.isBlank(req.getClassteamId())) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
       
        AppUserEntity user =  appUserService.getOne(userId);  
        if(user == null)  throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
        GuarderEntity ge = new GuarderEntity();
        ge.setPhone(user.getPhone());
        GuarderEntity guarder = gs.findOne(Example.of(ge));
        if(guarder == null) {
        	
        	guarder = new GuarderEntity();
        	guarder.setPhone(user.getPhone());
        	gs.save(guarder);
        }
        //检查该用户已绑定孩子数量，超过3则不允许绑定
        GuarderStudentEntity gsex = new GuarderStudentEntity();
        gsex.setGuarderId(guarder.getId());
        long bindNum = gss.count(Example.of(gsex));
        if(bindNum >= 3) {
        	 throw new HttpClientException(ErrorMessages.BindStudentOverflow);
        }
        
        StudentEntity se = new StudentEntity();
        se.setSchoolId(req.getSchoolId());
        se.setClassteamid(req.getClassteamId());
        se.setSn(req.getSn());
        se.setName(req.getName());
        
        StudentEntity student = studentService.findOne(Example.of(se));
        if(student == null) {
        	throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
        }
        
        
       
        GuarderStudentEntity bind = new GuarderStudentEntity();
        bind.setGuarderId(guarder.getId());
        bind.setStudentId(student.getId());
        
        long exists = gss.count(Example.of(bind));
        if(exists > 0) {
        	throw new HttpClientException(ErrorMessages.ExistsDataException);
        }
        
        gss.save(bind);

      
    }

    /**
     * 用户将指定的孩子与自己解绑
     *
     * @param userId
     * @param studentid
     */
    @DeleteMapping("/user/{userid}/child")
    public void releaseChild(@PathVariable("userid") String userId, @RequestParam("studentid") String studentid) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(studentid)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
        AppUserEntity user =  appUserService.getOne(userId);  
        if(user == null)  throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
        GuarderEntity ge = new GuarderEntity();
        ge.setPhone(user.getPhone());
        
        GuarderEntity guarder = gs.findOne(Example.of(ge));
        if(guarder != null) {
        	 GuarderStudentEntity gsex = new GuarderStudentEntity();
             gsex.setGuarderId(guarder.getId());
             gsex.setStudentId(studentid);
             gsex = gss.findOne(Example.of(gsex));
             if(gsex != null) {
            	 gss.delete(gsex);
             }
        }
    }

    /**
     * 获取用户已绑定孩子列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/user/{userId}/child")
    public List<StudentRowResponse> listChild(@PathVariable("userId") String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new HttpClientException(ErrorMessages.MissRequiredParameter);
        }
       AppUserEntity user =  appUserService.getOne(userId);  
       if(user == null)  throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
     
       List<StudentRowResponse> resp = new ArrayList<>();
       
       GuarderEntity ge = new GuarderEntity();
       ge.setPhone(user.getPhone());
       
       GuarderEntity g = gs.findOne(Example.of(ge));
       if(g != null) {
    	   GuarderStudentEntity gsex = new GuarderStudentEntity();
           gsex.setGuarderId(g.getId());
           
           List<GuarderStudentEntity> list =  gss.findAll(Example.of(gsex));
           
           List<String> ids = list.stream().map(GuarderStudentEntity::getStudentId).collect(Collectors.toList());
           
           List<StudentEntity> sList = studentService.findAllById(ids);
           
          resp = ListCopy.copyListProperties(sList, StudentRowResponse::new);

           for (StudentRowResponse srr: resp) {
               SchoolClassTeamEntity sct = classTeamService.getOne(srr.getClassteamid());
               String classteamName = DateUtil.getGrade(sct.getEndYear(), sct.getStage()).getTitle() + String.valueOf(sct.getOrderValue()) + "班";
               srr.setClassteamName(classteamName);
               SchoolEntity se = schoolService.getOne(srr.getSchoolId());
               srr.setSchoolName(se.getName());
           }
       }

       return resp;
    }
}
