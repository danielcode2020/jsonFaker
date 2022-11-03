package com.example.jsonfaker.service;

import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService implements UserDetailsService {
    private final SystemUserRepository systemUserRepository;
    private final UsersRepository usersRepository;

    public LoginUserService(SystemUserRepository systemUserRepository, UsersRepository usersRepository) {
        this.systemUserRepository = systemUserRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return systemUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with this username not found"));
    }

}
