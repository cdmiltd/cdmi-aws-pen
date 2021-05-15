package pw.cdmi.aws.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableAsync
@SpringBootApplication
@EnableScheduling
@ComponentScans({@ComponentScan("pw.cdmi.aws.micro.s3.rc")})
public class MainApplition {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(MainApplition.class, args);
        System.out.println("*****************************************************");
        System.out.println("*                                                   *");
        System.out.println("*             教育行业模块教材服务启动                 *");
        System.out.println("* 包含教材信息、教材知识点信息、教材练习题信息           *");
        System.out.println("*                                                   *");
        System.out.println("*****************************************************");
    }
   
}
