package pw.cdmi.aws.edu.test.modules.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.Semester;

import javax.persistence.*;

/**
 * 试题练习答卷
 */
@Data
@Entity(name = "edu_test_paper")
public class TestPaperEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;                      //试题练习记录编号
    private String studentId;               //对应的学生编号
    private String classteamId;             //对应的班级编号
    private String teacherId;               //批改老师编号
    private String bookId;                  //对应的教辅材编号
    @Column(name = "course")
    @Enumerated(EnumType.STRING)
    private DefaultCourse course;           //对应的学科
    private int classhourOrder;             //对应的课时序列
    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private GlobalGradeStage stage;         //年级
    @Column(name = "semester")
    @Enumerated(EnumType.STRING)
    private Semester semester;              //学期
    private int score;                      //得分
    private int rightNum;                   //做正确的题的数量
    private int errorNum;                   //做错误的题的数量
    private int halferrorNum;               //半正确的题的数量
    private String pdfUrl;                  //答卷pdf访问地址
}
