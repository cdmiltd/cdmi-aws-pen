package pw.cdmi.aws.edu.book.modules.entities;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 试题(占无)
 * @author Administrator
 *
 */
@Data
@Entity(name = "edu_book_exercise")
public class BookExerciseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "Id", length = 32)
    private String id;                      //试题编号
    private String bookId;                  //书籍编号
    private String origin;                  //试题来源,对应ExerciseOrigin的code
    private int pageNumber;                 //试题所在页码
//    private String classHour;             //试题所在课时数(第32课时)
    private int classHourNumber;            //试题所在课时数(32)
//    private String chapter;               //试题所在章节名
    private int chapterNumber;              //试题所在章节编号
    private String knowledges;              //试题知识点列表
    private String knowledgeNumbers;        //试题知识点编号列表
    private String content;                 //试题内容
    private String options;                 //试题选项（判断题，多选题等）
    private String catalog;                 //试题分类(计算，推理，应用，探究,根据科目自定义)
    private String answer;                  //试题答案（不同试题类型，答案格式不同）
    private String typeCode;                //试题类型，对应ExerciseType的Code属性
    private String difficultyCode;          //试题难度，对应ExerciseDifficulty的Code属性
    private int bigTestNumber;              //大题号
    private int smallTestNumber;            //小题号

    public String getString4ClassHour(){
        return "第" + this.classHourNumber + "课时";
    }

    public String[] getString4Knowledges(){
        if(StringUtils.isBlank(knowledges)){
            return new String[0];
        }else {
            return knowledges.split(",");
        }
    }
}
