package com.wh.yaofangwang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@MapperScan("com.wh.yaofangwang.mapper")
public class YaofangwangApplication {

    public static void main(String[] args) {
        SpringApplication.run(YaofangwangApplication.class, args);
    }

}
