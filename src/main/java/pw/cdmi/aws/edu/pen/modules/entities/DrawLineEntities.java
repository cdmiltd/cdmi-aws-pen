package pw.cdmi.aws.edu.pen.modules.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 笔
 * @author wangbojun
 *
 */
@Data
@Entity(name = "edu_draw_line")
public class DrawLineEntities {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;              //记录唯一标识
    @Column(length = 17)
    private String mac;             //Mac地址
    private String idCardStudentId;      //批改版绑定学生编号
    private String studentId;           //学生编号
    private Long pageNo;
    private String pointDatas;       //坐标点
    private Boolean state;          //是否统计
    private Date createTime;        //创建时间
    private Date updateTime;        //更新时间
    private Date statisTime;        //统计时间
    private Integer correctCount;    //正确个数
    private Integer errorCount;      //错误个数
    private Integer problemCount;       //问题个数
}
