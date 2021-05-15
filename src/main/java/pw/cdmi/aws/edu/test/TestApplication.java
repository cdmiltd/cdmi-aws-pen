package pw.cdmi.aws.edu.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

//@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(TestApplication.class, args);
        System.out.println("*****************************************************");
        System.out.println("*                                                   *");
        System.out.println("*         教育行业模块作业批改服务启动                 *");
        System.out.println("*   包含作业生成，作业批改、学情统计等信息              *");
        System.out.println("*                                                   *");
        System.out.println("*****************************************************");
    }
}
