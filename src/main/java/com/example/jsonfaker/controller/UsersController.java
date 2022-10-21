package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.service.UsersServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
    private UsersServiceImpl usersService;

    public UsersController(UsersServiceImpl usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/add")
    public Long saveUser(@RequestBody Users user){
        return usersService.saveUser(user);
    }

    @GetMapping("/all")
    public List<Users> getAllUsers(){
        return usersService.getAllUsers();
    }

}
