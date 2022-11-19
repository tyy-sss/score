package com.example.go;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.go.Mapper")
public class GoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoApplication.class, args);
    }

}
