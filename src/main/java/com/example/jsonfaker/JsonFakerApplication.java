package com.example.jsonfaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class JsonFakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonFakerApplication.class, args);
    }
}
