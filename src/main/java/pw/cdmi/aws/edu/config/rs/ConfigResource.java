package pw.cdmi.aws.edu.config.rs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.aws.edu.config.modules.ObsConfigEntity;
import pw.cdmi.aws.edu.config.modules.SmsConfigEntity;
import pw.cdmi.aws.edu.config.service.ObsConfigService;
import pw.cdmi.aws.edu.config.service.SmsConfigService;

@RestController
@RequestMapping("/app/v1")
public class ConfigResource {
	
	@Autowired
	private ObsConfigService obsService;
	
	@Autowired
	private SmsConfigService smsService;
	
	private String  defaultid = "100000";

	
	@Value("${system.version}")
	public String version;
	
	@PutMapping("/config/obs")
	public void updateObs(@RequestBody ObsConfigEntity e) {
		e.setId(defaultid);
		obsService.saveAndFlush(e);
	}
	@PutMapping("/config/sms")
	public void updateSms(@RequestBody SmsConfigEntity e) {
		e.setId(defaultid);
		smsService.saveAndFlush(e);
	}
	
	
	@GetMapping("/version")
	public String version() {
		return version;
	}
	
	@GetMapping("/config/obs")
	public ObsConfigEntity getObs() {
		
		return obsService.getOne(defaultid);
	}
	@GetMapping("/config/sms")
	public SmsConfigEntity getSms() {
		
		return smsService.getOne(defaultid);
	}
	
	
}
