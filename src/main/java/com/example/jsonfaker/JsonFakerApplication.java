package com.example.jsonfaker;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPid;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JsonFakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonFakerApplication.class, args);
        System.out.println((new ApplicationPid()));
    }
}
