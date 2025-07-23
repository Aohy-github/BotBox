package com.kob;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.kob.backend.mapper")
@Configuration
@EnableAspectJAutoProxy
public class BotBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotBoxApplication.class, args);
    }

}
