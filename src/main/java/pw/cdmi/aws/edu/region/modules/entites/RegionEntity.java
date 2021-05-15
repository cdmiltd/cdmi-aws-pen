package pw.cdmi.aws.edu.region.modules.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity(name = "edu_region")
public class RegionEntity {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", length = 32)
	private String id;

	private String code;

	private String name;
	
	private String parentCode;
}
