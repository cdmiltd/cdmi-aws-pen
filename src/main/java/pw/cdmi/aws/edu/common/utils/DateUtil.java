package pw.cdmi.aws.edu.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

import pw.cdmi.aws.edu.school.modules.GlobalGradeStage;
import pw.cdmi.aws.edu.school.modules.StudyStage;

public class DateUtil {
	
	
	public static final FastDateFormat yyyMMdd = FastDateFormat.getInstance("yyyy-MM-dd");

	/**
	 * 获取当前月份
	 * @return
	 */
	public static int getMonth() {
		 Calendar c = Calendar.getInstance();
		 return c.get(Calendar.MONTH) + 1;
	}
	
	public static int getYear() {
		 Calendar c = Calendar.getInstance();
		 return c.get(Calendar.YEAR);
	}
	
	public static String getCurrentDay() {
		return yyyMMdd.format(new Date());
	}
	
	/**
	 * 根据当前年份和年级计算 毕业年份
	 * @param month
	 * @param gradeStage
	 * @return
	 */
	public static int getEndYear(GlobalGradeStage gradeStage) {
		int year = getYear();
		int sumYear = gradeStage.getStudyStage().getYear();  //当前年级在所属学部一共学习多少年  小学6年  初高 3年
		int level = gradeStage.getLevel();   
		int endYear = sumYear - level + year;
		//7月份后的 入学年级为今年  毕业年就+1
		if(getMonth() >= 7) {
			return endYear + 1;
		}else {
			return endYear;
		}
	}
	
	/**
	 * 根据毕业年 和 学部计算当前年级
	 * @param endYear   毕业年
	 * @param stage     学部
	 * @return
	 */
	public static GlobalGradeStage getGrade(int endYear,StudyStage stage) {
		int year = getYear();
		int sumYear = stage.getYear(); 
		int level = sumYear - endYear + year;
//		if(level>sumYear) level = sumYear; 
		
		return GlobalGradeStage.getGradeStages(stage).stream().filter(e->e.getLevel().equals(level)).findFirst().get();
	}

}
