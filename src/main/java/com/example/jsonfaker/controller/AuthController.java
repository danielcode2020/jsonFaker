package com.example.jsonfaker.controller;

import com.example.jsonfaker.model.dto.JwtToken;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.model.dto.VerifyRequest;
import com.example.jsonfaker.service.Exporter;
import com.example.jsonfaker.service.UserAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final Exporter exporter;

    private final UserAuthService userAuthService;

    public AuthController(Exporter exporter, UserAuthService userAuthService) {
        this.exporter = exporter;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.debug("loginRequest : {}", loginRequest);
        var response = userAuthService.login(loginRequest);
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
    public ResponseEntity<JwtToken> authenticateUser2FA(HttpServletRequest request, @Valid @RequestBody VerifyRequest verifyRequest) throws Exception {
        System.out.println(request.getHeader("SessionKey"));
        var response = userAuthService.verify(request.getHeader("SessionKey"), verifyRequest.code());
        return ResponseEntity
                .ok()
                .body(response);
    }



}
