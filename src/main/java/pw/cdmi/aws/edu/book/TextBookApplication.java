package pw.cdmi.aws.edu.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.io.IOException;

//@SpringBootApplication
//@ComponentScans({@ComponentScan("pw.cdmi.aws.micro.s3.rc"),@ComponentScan("pw.cdmi.aws.edu.common")})
public class TextBookApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(TextBookApplication.class, args);
        System.out.println("*****************************************************");
        System.out.println("*                                                   *");
        System.out.println("*             教育行业模块教材服务启动                 *");
        System.out.println("* 包含教材信息、教材知识点信息、教材练习题信息           *");
        System.out.println("*                                                   *");
        System.out.println("*****************************************************");
    }
}
