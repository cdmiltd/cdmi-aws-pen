package pw.cdmi.aws.edu.guarder.rs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import pw.cdmi.aws.edu.common.enums.ErrorMessages;
import pw.cdmi.aws.edu.common.utils.ListCopy;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderEntity;
import pw.cdmi.aws.edu.guarder.modules.entities.GuarderStudentEntity;
import pw.cdmi.aws.edu.guarder.rs.response.GuarderDetailResponse;
import pw.cdmi.aws.edu.guarder.rs.response.GuarderRequest;
import pw.cdmi.aws.edu.guarder.service.GuarderService;
import pw.cdmi.aws.edu.guarder.service.GuarderStudentService;
import pw.cdmi.aws.edu.school.modules.entities.StudentEntity;
import pw.cdmi.aws.edu.school.services.StudentService;
import pw.cdmi.core.exception.HttpClientException;

@RestController
@RequestMapping("/edu/v1")
@Tag(name = "edu", description = "教育模块")
public class GuarderResource {
	
	@Autowired
	GuarderStudentService gss;
	
	@Autowired
	GuarderService gs;
	
	@Autowired
	StudentService studentSerivce;
	
	
	/**
     * 获取监护人的孩子列表
     *
     * @param guarderid
     * @return 返回监护人的孩子列表
     */
    @GetMapping("/guarder/{guarderid}/student")
    public List<StudentEntity> listChildrenByGuarder(@PathVariable(name = "guarderid") String guarderid) {
    	
    	GuarderStudentEntity se = new GuarderStudentEntity();
    	se.setGuarderId(guarderid);
    	List<GuarderStudentEntity>   list = gss.findAll(Example.of(se));
    	
    	List<String> ids = list.stream().map(GuarderStudentEntity::getStudentId).collect(Collectors.toList());
    	
    	if(!ids.isEmpty()) {
    		return studentSerivce.findAllById(ids);
    	}
    	
        return new ArrayList<>();
    }
    
    /**
     * 获取指定孩子的监护人列表
     *
     * @param childid
     * @return 返回孩子的监护人列表
     */
    @GetMapping("/guarder")
    public List<GuarderDetailResponse> listGuarderDetail(@RequestParam(name = "studentid") String studentid) {
    	
    	
    	GuarderStudentEntity se = new GuarderStudentEntity();
    	se.setStudentId(studentid);
    	List<GuarderStudentEntity>   list = gss.findAll(Example.of(se));
    	
    	Map<String, GuarderStudentEntity> map = list.stream().collect(Collectors.toMap(GuarderStudentEntity::getGuarderId, e->e,(e1,e2)->e1));
    
    	if(!map.isEmpty()) {
    		List<GuarderEntity> gelist = gs.findAllById(map.keySet());
    		
    		List<GuarderDetailResponse> result = ListCopy.copyListProperties(gelist, GuarderDetailResponse::new);
    		
    		result.forEach(e->{
    			e.setRelation(map.get(e.getId()).getRelation());
    		});
    		
    		return result;
    	}
    	
        return new ArrayList<>();
    	
    }
    
    /**
     * 获取指定监护人详情信息
     *
     * @param guarderid
     * @return 返回监护人详细信息
     */
    @GetMapping("/guarder/{guarderid}")
    public GuarderDetailResponse getGuarderDetail(@PathVariable(name = "guarderid") String guarderid,
                                                  @RequestParam(name = "studentid",required = false) String studentid) {
    	
    	GuarderEntity ge = gs.getOne(guarderid);
    	if(ge == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
    	GuarderDetailResponse res = new GuarderDetailResponse();
    	res.setId(ge.getId());
    	res.setName(ge.getName());
    	res.setPhone(ge.getPhone());
    	
    	if(StringUtils.isNotBlank(studentid)) {
    		GuarderStudentEntity ex = new GuarderStudentEntity();
    		ex.setGuarderId(guarderid);
    		ex.setStudentId(studentid);
    		GuarderStudentEntity re = gss.findOne(Example.of(ex));
    		res.setRelation(re.getRelation());
    	}
        return res;
    }
	
    /**
     * 为学生增加一个监护人信息
     *
     * @param childid
     * @return 返回监护人账号ID
     */
    @PostMapping("/guarder")
    public String createGuarder(@RequestParam(name = "studentid") String studentid,@RequestBody GuarderRequest req) {
    	GuarderEntity ge= gs.createGuarder(studentid, req);
        return ge.getId();
    }

    /**
     * 编辑指定学生的指定家长信息
     *
     * @param childid
     * @return 返回监护人账号ID
     */
    @PutMapping("/guarder/{guarderid}")
    public void editGuarder(@PathVariable("guarderid") String guarderid,@RequestParam(name = "studentid") String studentid,
                            @RequestBody GuarderRequest req) {
    	
    	GuarderEntity ge =	gs.getOne(studentid);
    	
    	if(ge == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
    	
    	GuarderStudentEntity ex = new GuarderStudentEntity();
    	ex.setGuarderId(guarderid);
    	ex.setStudentId(studentid);
    	
    	GuarderStudentEntity res = gss.findOne(Example.of(ex));
    	if(res == null) throw new HttpClientException(ErrorMessages.NotFoundObjectKey);
    	
    	
    	gs.modifyGuarder(res, ge, req);
    	
        return;
    }

   

   

    
}
