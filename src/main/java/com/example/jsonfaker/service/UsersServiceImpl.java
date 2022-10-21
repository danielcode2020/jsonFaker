package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Long saveUser(Users user) {
        return usersRepository.save(user).getId();
    }

    @Override
    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        usersRepository.findAll().forEach(users::add);
        return users;
    }


}
