package pw.cdmi.aws.edu.school.rs.responses;

import lombok.Data;
import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.StudyStage;

@Data
public class TeacherRowResponse {
	private String id;

	private String name; // 姓名

	private String sex; // 性别

	private DefaultCourse course; // 学科

	private StudyStage stage; // 小学还是初中

	private String faceImage; // 头像

	private String phone; // 数字，联系电话

	private Integer age; // 年龄

	private String text; // 简介

	/**
	 * 教师对应的学校标识
	 */
	private String schoolId;
	
	private SchoolDetaiResponse school;

	/**
	 * 信息归属应用唯一标识
	 */
	private String appId;

	/**
	 * 信息归属租户唯一标识
	 */
	private String tenantId;

	public String getCourseTitle() {
		if (course != null)
			return this.course.getTitle();
		return null;
	}

	public String getStageTitle() {
		if (this.stage != null)
			return this.stage.getTitle();
		return null;
	}

}
