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
@Entity(name ="edu_huawei_obs_config")
public class ObsConfigEntity {

	@Id
    @Column(name = "Id", length = 32)
    private String id; 
		
	private String ak;
	
	private String sk;
	
	private String endPoint;
	
	private String bucketName;
}
