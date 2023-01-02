package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.model.dto.VerifyRequest;
import com.example.jsonfaker.service.Exporter;
import com.example.jsonfaker.service.UserAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    private final Exporter exporter;

    private final UserAuthService userAuthService;

    public AuthController(Exporter exporter, UserAuthService userAuthService) {
        this.exporter = exporter;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String response = userAuthService.login(loginRequest);
        return ResponseEntity
                .ok()
                .body(response);
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

    @PostMapping("/verify")
    public ResponseEntity<String> authenticateUser2FA(@Valid @RequestBody VerifyRequest verifyRequest) throws Exception {
        String response = userAuthService.verify(verifyRequest.getUsername(), verifyRequest.getCode());
        return ResponseEntity
                .ok()
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (nonNull(auth)){
            new SecurityContextLogoutHandler().logout(request,response,auth);
            return ResponseEntity.ok().body("logout successfully");
        }
        return ResponseEntity.ok().body("unable to logout");
    }



}
