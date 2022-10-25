package com.example.jsonfaker.controller;

import com.example.jsonfaker.configuration.AppProperties;
import com.example.jsonfaker.configuration.DbStartupConfig;
import com.example.jsonfaker.enums.Role;
import com.example.jsonfaker.model.Address;
import com.example.jsonfaker.model.Company;
import com.example.jsonfaker.model.Geo;
import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.repository.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import static com.example.jsonfaker.enums.Role.ANONYMOUS_USER;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class FackerController {

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final AppProperties customProps;
    private final UsersRepository usersRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public FackerController(Logger logger, ObjectMapper objectMapper, RestTemplate restTemplate,
                            AppProperties customProps, UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.customProps = customProps;
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/populate")
    public ResponseEntity getData() throws JsonProcessingException {
        String userPassword = bCryptPasswordEncoder.encode("user");
        ResponseEntity<Object[]> response = restTemplate.getForEntity(customProps.getUri(), Object[].class);
        List<Users> users = Arrays.stream(response.getBody())
                .map(obj -> objectMapper.convertValue(obj, Users.class))
                .collect(Collectors.toList());
        for (Users user: users) {
            user.setRole(ANONYMOUS_USER);
            user.setPassword(userPassword); //password
        }

        Geo geoAdmin = new Geo(2.3, 3.4);
        Address addressAdmin = new Address("streetx","suitex","cityx", "zipcodex",geoAdmin);
        Company companyAdmin = new Company("esempla","catchpx","bsx");
        Users admin = new Users("admin","admin",bCryptPasswordEncoder.encode("admin"), Role.ADMIN,"admin@gmail.com","1234","ww.rand",addressAdmin,companyAdmin);

        users.add(admin);
        usersRepository.saveAll(users);
        logger.info("succesfully saved");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/add")// for testing validation
    public ResponseEntity addUser(@Valid @RequestBody Users user){
        usersRepository.save(user);
        return ResponseEntity.ok("saved");
    }
}
