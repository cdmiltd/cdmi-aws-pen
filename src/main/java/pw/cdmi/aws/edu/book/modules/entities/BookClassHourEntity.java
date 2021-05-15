package pw.cdmi.aws.edu.book.modules.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 书本课时
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_book_classhour")
public class BookClassHourEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;                      //书本课时编号
    private String bookId;                  //书本编号
    private Integer orderValue;              //课时序列号
    
    private String subtitle; 				//课时名称
    
    
    private Long beginPageNo;
    
    private Long endPageNo;
    
    private String knowledges;				//课时对应的知识点
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;
    
   
}
