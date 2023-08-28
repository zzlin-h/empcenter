package com.zzl.empcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zzl.empcenter.mapper")
public class EmpcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpcenterApplication.class, args);
    }

}
