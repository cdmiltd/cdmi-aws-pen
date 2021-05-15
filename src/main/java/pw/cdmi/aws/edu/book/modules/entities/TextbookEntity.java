package pw.cdmi.aws.edu.book.modules.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;
import pw.cdmi.aws.edu.book.modules.PageSize;
import pw.cdmi.aws.edu.book.modules.TextbookFormat;
import pw.cdmi.aws.edu.book.modules.TextbookSystem;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.Semester;
import pw.cdmi.aws.edu.school.modules.StudyStage;

/**
 * 教材
 */
@Data
@Entity(name = "edu_book")
@DynamicInsert
@DynamicUpdate   //动态更新属性
@ToString
public class TextbookEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;              //记录唯一标识
    @Column(name = "name",length = 36)
    private String name;            //书名
    @Column(name = "code")
    private String code;            //书籍编号
    @Column(name = "publisher")
    private String publisher;       //出版社
    @Column(name = "system")
    @Enumerated(EnumType.STRING)
    private TextbookSystem system;  //教材体系
    @Column(name = "format")
    @Enumerated(EnumType.STRING)
    private TextbookFormat format;  //载体类别   书籍/试卷
    @Column(name = "pdfurl", length = 320)
    private String pdfUrl;          //该书pdf文件
    @Column(name = "course")
    @Enumerated(EnumType.STRING)
    private DefaultCourse course;   //学科
    
    @Column(name = "image", length = 320)
    private String image;  //封面图片

    @Column(name = "stage")
    @Enumerated(EnumType.STRING)
    private StudyStage stage;             //适合学部阶段
    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private GlobalGradeStage grade;       //适合年级
    @Column(name = "semester")
    @Enumerated(EnumType.STRING)
    private Semester semester;            //选择学期

    @Column(name = "pagesize")
    @Enumerated(EnumType.STRING)
    private PageSize pageSize;            //书页大小
    @Column(name = "start_pageno")
    private Long startPageno;              //开始页号(点读笔可识别的页号)
    @Column(name = "end_pageno")
    private Long endPageno;                //结束页号
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;
    
    /**
     * 试卷属性
     */
    @Column(name = "test_book_id")
    private String testBookId;          //试卷书籍id

    

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
    
    
    
    @Transient
    private TextbookEntity testBook;   //教材的试卷信息
    
    
    @Column(name="identity_rangle")
    private String identityRangle; //学号区坐标 四个物理长度坐标
    
    @Column(name="score_rangle")
    private String scoreRangle; //的分区坐标
    
    
    public String getCourseText() {
    	if(this.course != null) {
    		return this.course.getTitle();
    	}
    	return null;
    }
    
    public String getStageText() {
    	if(this.stage != null) return this.stage.getTitle();
    	
    	return null;
    }
    
    public String getGradeText() {
    	if(this.grade != null) return this.grade.getTitle();
    	return null;
    }
    
    public String getSemesterText() {
    	if(this.semester != null) return this.semester.getTitle();
    	return null;
    }
}
