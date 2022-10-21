package com.example.jsonfaker.controller;

import com.example.jsonfaker.configuration.AppProperties;
import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.repository.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FackerController {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final AppProperties customProps;
    private final UsersRepository usersRepository;

    public FackerController(ObjectMapper objectMapper, RestTemplate restTemplate,
                            AppProperties customProps, UsersRepository usersRepository) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.customProps = customProps;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/populate")
    public ResponseEntity getData() throws JsonProcessingException {
        ResponseEntity<Object[]> response = restTemplate.getForEntity(customProps.getUri(), Object[].class);
        List<Users> users = Arrays.stream(response.getBody())
                .map(obj -> objectMapper.convertValue(obj, Users.class))
                .collect(Collectors.toList());
        usersRepository.saveAll(users);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/add")// for testing validation
    public ResponseEntity addUser(@Valid @RequestBody Users user){
        usersRepository.save(user);
        return ResponseEntity.ok("saved");
    }
}
