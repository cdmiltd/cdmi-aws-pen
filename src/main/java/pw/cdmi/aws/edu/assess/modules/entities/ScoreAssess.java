package pw.cdmi.aws.edu.assess.modules.entities;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "edu_score_assess")
@DynamicInsert
@DynamicUpdate   //动态更新属性
@ToString
public class ScoreAssess {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;              //记录唯一标识
    private String studentId;       //对应的学生标识
    private String classTeamId;     //对应的学生班级标识,用于班级内统计
    private Date assessDate;        //批改时间
    private Date modifyDate;  		//最后修改时间
    private String textBookId;      //试卷编号
    private Integer score;          //分数
}
