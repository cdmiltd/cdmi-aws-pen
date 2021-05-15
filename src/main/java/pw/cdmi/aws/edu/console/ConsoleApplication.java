package pw.cdmi.aws.edu.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.io.IOException;

//@SpringBootApplication
//@ComponentScans({@ComponentScan("pw.cdmi.aws.edu.huawei"),@ComponentScan("pw.cdmi.aws.edu.common")})
public class ConsoleApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ConsoleApplication.class, args);
        System.out.println("*****************************************************");
        System.out.println("*                                                   *");
        System.out.println("*             教育行业用户服务启动                 *");
        System.out.println("*                包含用户管理                    *");
        System.out.println("*                                                   *");
        System.out.println("*****************************************************");
    }
}
