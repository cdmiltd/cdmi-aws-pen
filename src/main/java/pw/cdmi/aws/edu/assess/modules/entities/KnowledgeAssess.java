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
@Entity(name = "edu_knowledge_assess")
@DynamicInsert
@DynamicUpdate   //动态更新属性
@ToString
public class KnowledgeAssess {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;              //记录唯一标识
    private String knowledgeId;     //对应的知识点标识
    private String studentId;       //对应的学生标识
    private String classTeamId;     //对应的学生班级标识,用于班级内统计
    private Date assessDate;        //批改时间
    
    private Date modifyDate;  		//最后修改时间
    private int correctCount;       //正确数量
    private int errorCount;         //错误数量
    private int problemCount;       //问题数量(半正确的数量)
}
