package pw.cdmi.aws.edu.school.modules.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.modules.StudyTimeUnits;

/**
 * 学校班级(如：小学五年级四班)
 */
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_school_classteam")
public class SchoolClassTeamEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;

    /**
     * 对应学校唯一标识
     */
    @Column(name = "school_id", nullable = false)
    private String schoolId;

    /**
     * 班级序号
     */
    @Column(name = "order_value", nullable = false)
    private Integer orderValue;
    
    /**
     * 班级名称，可不设置。
     */
    @Column(name = "name")
    private String name;
    
    /**
     * 班主任，对应TeacherID。
     */
    @Column(name = "adviser_id")
    private String adviser;

    /**
     * 班级学生数量
     */
    @Column(name = "student_num")
    private Integer studentNum;
    
    /**
     * 班级对应的学部  小学/初中/高中
     */
    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private StudyStage stage;
    
	 /**
	 * 毕业年份  小/初/高  2020级  (配合stage查询)
	 */
	 @Column(name="classend")     
	 private Integer endYear;
	 
	 
	 
	
    

    /**
     * 班级学习时长
     */
    @Column(name = "timeunit", nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyTimeUnits timeunit = StudyTimeUnits.YEAR;

    
    

    /**
     * 班级创建时间
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "createdDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;


    
    private Long idCardSn;   //批改版序列号 sn 只记录最新的一次
    
    
    @Column(name="encode_idcard_pdf_path",length = 1024)
    private String encodeIdCardPdfPath;   //编码后的 pdf 文件地址
    
    /**
     * 信息归属应用唯一标识
     */
    @Column(name = "app_id",length = 32)
    private String appId;

    /**
     * 信息归属租户唯一标识
     */
    @Column(name = "tenant_id",length = 32)
    private String tenantId;
    
    
//    /**
//     * 得到班级名字
//     *
//     * @return
//     */
//    public String getName() {
//        if (StringUtils.isBlank(this.name)) {
//            if (StudyTimeUnits.YEAR == this.timeunit) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(createdDate);
//                int year = calendar.get(Calendar.YEAR); //建班年
//                Date now = new Date();
//                calendar = Calendar.getInstance();
//                calendar.setTime(now);
//                int now_year = calendar.get(Calendar.YEAR);//当前年
//                int interval = now_year - year; //与建班年相关年数
//                String changetime = now_year + "-09-01 00:00:00";  //
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                try {
//                    Date changedate = df.parse(changetime);
//                    if(now.compareTo(changedate) > 1){ //9月1日之后
//                        interval = interval + 1;
//                    }
//                }catch (ParseException ex){
//                    //那么就显示多少级多少班
//                    return String.valueOf(now_year).substring(2)+"级" + orderValue + "班";
//                }
//                if(StringUtils.isBlank(gradeStage)){
//                    if(StudyStage.PrimarySchool == StudyStage.valueOf(gradeStage)){
//                        if (interval <= StudyStage.PrimarySchool.getYear()) {
//                            return StudyStage.PrimarySchool.getTitle() + interval + "年级" + orderValue + "班";
//                        } else {
//                            return StudyStage.PrimarySchool.getTitle() + String.valueOf(year).substring(2) + "届" + orderValue + "班";
//                        }
//                    }else if(StudyStage.JuniorHighSchool == StudyStage.valueOf(gradeStage)) {
//                        if (interval <= StudyStage.JuniorHighSchool.getYear()) {
//                            return StudyStage.JuniorHighSchool.getTitle() + interval + "年级" + orderValue + "班";
//                        } else {
//                            return StudyStage.JuniorHighSchool.getTitle() + String.valueOf(year).substring(2) + "届" + orderValue + "班";
//                        }
//                    }else if(StudyStage.HighSchool == StudyStage.valueOf(gradeStage)){
//                        if (interval <= StudyStage.HighSchool.getYear()) {
//                            return StudyStage.HighSchool.getTitle() + interval + "年级" + orderValue + "班";
//                        } else {
//                            return StudyStage.HighSchool.getTitle() + String.valueOf(year).substring(2) + "届" + orderValue + "班";
//                        }
//                    }else{
//                        return String.valueOf(year).substring(2)+"级" + orderValue + "班";
//                    }
//                }
//            }
//            if(StringUtils.isBlank(schoolCourseName)){
//                return orderValue + "班";
//            }else{
//                return schoolCourseName + orderValue + "班";
//            }
//        }else{
//            return this.name;
//        }
//    }
}
