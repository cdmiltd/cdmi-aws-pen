package pw.cdmi.aws.edu.config.modules;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 云存储配置
 * @author Administrator
 *
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name ="edu_huawei_sms_config")
public class SmsConfigEntity {

	@Id
    @Column(name = "Id", length = 32)
    private String id = "100000"; 
		
	private String ak;
	
	private String sk;
	
	private String regionName;
	
	private String signId;
}
