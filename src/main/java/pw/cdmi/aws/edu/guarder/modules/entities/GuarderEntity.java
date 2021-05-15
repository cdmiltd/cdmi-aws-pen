package pw.cdmi.aws.edu.guarder.modules.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 学生监护人
 */
@Data
@Entity(name = "edu_guarder")
public class GuarderEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 188L;

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "name", length = 18)
    private String name;

    @Column(name = "phone",unique = true)
    private String phone;        //数字，联系电话

    /**
     * 信息归属应用唯一标识
     */
    @Column(name = "app_id", length = 32)
    private String appId;

    /**
     * 信息归属租户唯一标识
     */
    @Column(name = "tenant_id", length = 32)
    private String tenantId;

}
