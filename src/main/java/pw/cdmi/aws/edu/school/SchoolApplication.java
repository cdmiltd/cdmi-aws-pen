package pw.cdmi.aws.edu.school;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

//@SpringBootApplication
//@ComponentScans({@ComponentScan("pw.cdmi.aws.micro.s3.rc"),@ComponentScan("pw.cdmi.aws.edu.common")})
public class SchoolApplication {

    public static void main(String[] args) throws IOException {
    	SpringApplication.run(SchoolApplication.class, args);
		System.out.println("*****************************************************");
		System.out.println("*                                                   *");
		System.out.println("*             教育行业模块学校数据服务启动             *");
		System.out.println("* 包含学校信息、班级信息、教师信息、学生信息及相关信息    *");
		System.out.println("*                                                   *");
		System.out.println("*****************************************************");
	}
}
