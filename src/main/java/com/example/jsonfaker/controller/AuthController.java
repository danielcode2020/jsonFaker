package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.TokenResponse;
import com.example.jsonfaker.repository.UsersRepository;
import com.example.jsonfaker.security.jwt.JwtUtils;
import com.example.jsonfaker.service.LoginUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
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
    public TokenResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SystemUser userDetails = (SystemUser) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        return new TokenResponse(jwt);
    }


}
