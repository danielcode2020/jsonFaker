package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.repository.UsersRepository;
import com.example.jsonfaker.security.jwt.JwtUtils;
import com.example.jsonfaker.service.LoginUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final LoginUserService loginUserService;
    private final UsersRepository usersRepository;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, LoginUserService loginUserService, UsersRepository usersRepository, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.loginUserService = loginUserService;
        this.usersRepository = usersRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Users userDetails = (Users) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.status(301).headers(httpHeaders)
                .body("signed in");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.status(301).headers(httpHeaders)
                .body("signed out");
    }

}