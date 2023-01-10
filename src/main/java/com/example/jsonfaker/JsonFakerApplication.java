package com.example.jsonfaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JsonFakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonFakerApplication.class, args);
    }
}
