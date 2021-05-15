package pw.cdmi.aws.edu.idcard.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.ToString;

/**
 * 批改版绑定学生
 * @author Administrator
 *
 */
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@Entity(name="edu_id_card_student")
public class IdCardStudentEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 18238L;
	
	/**
	 * 批改版模板数据
	 */
	private static final float leftPadding = 1;    //左边距 厘米    300dpi 1厘米=118.11像素
	
	private static final float topPadding = 3.8f; 	//上边距        
	
	private static final float itemHeight = 1.64f; //每个格高度
	
	private static final float itemWidth = 4.76f; //2.38f;  //单元格宽度

	@Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id", length = 32)
    private String id;
	
	
	private Integer indexNum;   //下标
	
	private Integer studentSn; //学生学号
	
	private Long studentCardSn; //学生身份码
	
	@Column(name="cardId")
	private String  cardId;  //批改版id
	
	private String studentId;  //绑定的学生
	
	 @Column(name="create_date",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	    private java.util.Date createDate;
	 
	/**
	 * 学生区域坐标
	 */
	private Float upleftX;   
	
	private Float upleftY;
	
	private Float lowRightX;
	
	private Float lowRightY;
	
	
	/**
	 * 计算设置每个学生码的 对角区域坐标
	 */
	public void caclRangePoint() {
		int i = this.studentSn;
		int line = (int) Math.ceil(i/4.0); //第几排 
		int cell = (i % 4 == 0 ? 4 : i % 4);  //第几个
		float upleftX = ((cell - 1) *  itemWidth  + leftPadding) * 10;
		float lowRightX = (cell *  itemWidth  + leftPadding) * 10;
		
//		//加上边框
//		if(cell == 2) {
//			upleftX = upleftX + 1;
//			lowRightX = lowRightX + 3;
//		}else if (cell == 3) {
//			upleftX = upleftX + 3;
//			lowRightX = lowRightX + 5;
//		}else if (cell == 4) {
//			upleftX = upleftX + 5;
//			lowRightX = lowRightX + 7;
//		}
		
		float upleftY =( (line - 1) * itemHeight  + topPadding) * 10;
		
		float lowRightY =( line  * itemHeight  + topPadding) * 10;
		
		this.upleftX = upleftX;
		this.upleftY = upleftY;
		this.lowRightX = lowRightX;
		this.lowRightY = lowRightY;
		
}
	
}
