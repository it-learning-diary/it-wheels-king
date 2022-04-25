package cn.it.learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author it-learning-diary
 * @Description TODO
 * @Date 2022/4/25 10:57
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan({"cn.it.learning.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
