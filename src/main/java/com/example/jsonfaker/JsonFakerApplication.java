package com.example.jsonfaker;

import com.example.jsonfaker.repository.GeoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JsonFakerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonFakerApplication.class, args);
    }

}
