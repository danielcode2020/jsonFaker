package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.repository.UsersRepository;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody Users user) {
        return new ResponseEntity<>(usersRepository.save(user).getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<Users> users = new ArrayList<>();
        usersRepository.findAll().forEach(users::add);
        if (users.isEmpty()) {
            return new ResponseEntity<>("empty", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<String> updateUser(@Valid @RequestBody Users user) {
        if (usersRepository.existsById(user.getId())) {
            usersRepository.save(user);
            return new ResponseEntity<>("updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<?> getUserById(@Valid @PathVariable("id") Long id) {
        if (usersRepository.existsById(id)) {
            return new ResponseEntity<>(usersRepository.findById(id).stream().findFirst().get(), HttpStatus.FOUND);
        }
        return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<?> deleteUserById(@Valid @PathVariable("id") Long id) {
        if (usersRepository.existsById(id)) {
            usersRepository.deleteById(id);
            return new ResponseEntity<>("deleted", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
    }

}
