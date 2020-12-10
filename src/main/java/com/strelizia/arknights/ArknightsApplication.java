package com.strelizia.arknights;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages ={"com.strelizia.arknights.dao"})
@EnableSwagger2
@EnableScheduling
@EnableAsync
public class ArknightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArknightsApplication.class, args);
    }

}
