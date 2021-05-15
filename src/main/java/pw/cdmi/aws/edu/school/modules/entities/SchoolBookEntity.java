package pw.cdmi.aws.edu.school.modules.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 学校教材绑定
 */
@Data
@Entity(name = "edu_school_book")
public class SchoolBookEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;
    @Column(name = "schoolId", length = 32)
    private String schoolId;
    @Column(name = "bookId", length = 32)
    private String bookId;
    
    @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.util.Date createDate;
    
    private Boolean enable;
}
