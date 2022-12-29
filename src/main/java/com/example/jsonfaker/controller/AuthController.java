package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.model.dto.TokenResponse;
import com.example.jsonfaker.model.dto.VerifyRequest;
import com.example.jsonfaker.repository.RolesRepository;
import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.repository.UsersRepository;
import com.example.jsonfaker.security.jwt.JwtUtils;
import com.example.jsonfaker.service.Exporter;
import com.example.jsonfaker.service.LoginUserService;
import com.example.jsonfaker.service.UserAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Exporter exporter;

    private final UserAuthService userAuthService;

    public AuthController(AuthenticationManager authenticationManager, LoginUserService loginUserService, UsersRepository usersRepository, SystemUserRepository systemUserRepository, RolesRepository rolesRepository, JwtUtils jwtUtils, BCryptPasswordEncoder bCryptPasswordEncoder, Exporter exporter, UserAuthService userAuthService) {
        this.authenticationManager = authenticationManager;
        this.loginUserService = loginUserService;
        this.usersRepository = usersRepository;
        this.systemUserRepository = systemUserRepository;
        this.rolesRepository = rolesRepository;
        this.jwtUtils = jwtUtils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.exporter = exporter;
        this.userAuthService = userAuthService;
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

    @PostMapping("/register2FA")
    public ResponseEntity<byte[]> registerUser2FA(@Valid @RequestBody SignupRequest signupRequest) throws Exception {

        userAuthService.register2FA(signupRequest);
        byte[] qrCodeBytes = userAuthService.mfaAccountSetup(signupRequest.getUsername());

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\""+exporter.exportFileNameQR() + ".png\"")
                .body(qrCodeBytes);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) throws Exception {
        userAuthService.simpleRegister(signupRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

//    @PostMapping("/login2FA")
//    public String login2FA(@Valid @RequestBody LoginRequest loginRequest){
//
//    }

//    @PostMapping("/verify")
//    public TokenResponse authenticateUser2FA(@Valid @RequestBody VerifyRequest verifyRequest){
//
//    }


}
