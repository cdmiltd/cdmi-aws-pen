package pw.cdmi.aws.edu.pen.modules.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 笔
 * @author liwenping
 *
 */
@Data
@Entity(name = "edu_pen")
public class PenEntities {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;              //记录唯一标识
    @Column(length = 17,unique = true)
    private String mac;             //Mac地址
    
    private String sn;              //序列号
    
    @Temporal(TemporalType.DATE)
    @Column(name = "import_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date importDate;        //导入时间
    
    @Temporal(TemporalType.DATE)
    @Column(name = "active_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date activeDate;  		//激活时间
    
    @Column(name="activated",columnDefinition = "TINYINT default 0")
    private Boolean activated;      //是否已激活
    private String name;            //笔的名字
   
    private Double quantity;        //电量
  
    private String schoolId;        //学校名
    private String teacherId;       //教师名
}
