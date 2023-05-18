package com.example.jsonfaker.model.dto;

import com.example.jsonfaker.model.Users;

public record UserDto(String name, String username) {
    public UserDto(Users user){
        this(user.getName(), user.getUsername());
    }
}
