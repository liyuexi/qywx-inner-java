package com.tobdev.qywxinner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync
@MapperScan("com.tobdev.qywxinner.mapper")
@ServletComponentScan
public class DemoApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoApplication.class, args);
    }
    
}
