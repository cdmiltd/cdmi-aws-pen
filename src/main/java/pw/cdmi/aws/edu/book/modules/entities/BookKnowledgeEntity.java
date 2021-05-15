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
 * 书本知识点
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "edu_book_knowledge")
public class BookKnowledgeEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;                  //知识点编号
    private String bookId;              //书本编号
    private String title;               //知识点描述
    private Integer sortOrderValue;     //排序字段
    private String code;                //知识点编码
    
    private String pages;               //知识点对应的教材页码数组
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;
    

}
