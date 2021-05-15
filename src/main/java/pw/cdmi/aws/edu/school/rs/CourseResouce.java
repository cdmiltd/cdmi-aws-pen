package pw.cdmi.aws.edu.school.rs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.aws.edu.school.modules.DefaultCourse;
import pw.cdmi.aws.edu.school.modules.StudyStage;
import pw.cdmi.aws.edu.school.rs.responses.CourseRowResponse;

@RestController
@RequestMapping("/edu/v1")
public class CourseResouce {
	
	/**
	 * 获取学校指定学部的学科列表
	 * @param stage
	 * @return
	 */
	@GetMapping("course")
	public List<CourseRowResponse> getCourse(@RequestParam("stage") String stage){
		List<CourseRowResponse> resp = new ArrayList<>();
		String courses = StudyStage.valueOf(stage).getCourses();
		if(StringUtils.isNotBlank(courses)) {
			String[] arr = StringUtils.split(courses,",");
			for (String e : arr) {
				CourseRowResponse r = new CourseRowResponse();
				r.setKey(DefaultCourse.valueOf(e).name());
				r.setValue(DefaultCourse.valueOf(e).getTitle());
				resp.add(r);
			}
		}

		
		return resp;
		
	}
}
