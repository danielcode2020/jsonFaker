package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Users;

import java.util.List;

public interface UsersService{
    Long saveUser(Users user);
    List<Users> getAllUsers();

}
