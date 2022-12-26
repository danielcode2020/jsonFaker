package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.Roles;
import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.model.dto.TokenResponse;
import com.example.jsonfaker.repository.RolesRepository;
import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.repository.UsersRepository;
import com.example.jsonfaker.security.AuthoritiesConstants;
import com.example.jsonfaker.security.jwt.JwtUtils;
import com.example.jsonfaker.service.LoginUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final LoginUserService loginUserService;
    private final UsersRepository usersRepository;
    private final SystemUserRepository systemUserRepository;
    private final RolesRepository rolesRepository;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, LoginUserService loginUserService, UsersRepository usersRepository, SystemUserRepository systemUserRepository, RolesRepository rolesRepository, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.loginUserService = loginUserService;
        this.usersRepository = usersRepository;
        this.systemUserRepository = systemUserRepository;
        this.rolesRepository = rolesRepository;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest){

        Roles simpleUserRole = new Roles();
        simpleUserRole.setName(AuthoritiesConstants.USER);

        SystemUser user = new SystemUser();
        user.setPassword(signupRequest.getPassword());
        user.setUsername(signupRequest.getUsername());
        user.setAuthorities(rolesRepository.findAllByName("ROLE_USER").stream().collect(Collectors.toSet()));

//        Long id = systemUserRepository.save(user).getId();


        return new ResponseEntity<>(systemUserRepository.save(user).getId(), HttpStatus.CREATED);
    }


}
