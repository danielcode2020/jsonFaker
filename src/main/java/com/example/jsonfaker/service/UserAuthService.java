package com.example.jsonfaker.service;

import com.example.jsonfaker.controller.AuthController;
import com.example.jsonfaker.model.MfaLoginSession;
import com.example.jsonfaker.model.Roles;
import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.JwtToken;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.repository.MfaLoginSessionRepository;
import com.example.jsonfaker.repository.RolesRepository;
import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.security.AuthoritiesConstants;
import com.example.jsonfaker.security.jwt.JwtUtils;
import com.example.jsonfaker.twoFA.MFATokenManager;
import com.example.jsonfaker.twoFA.MfaTokenData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UserAuthService {
    private final Logger log = LoggerFactory.getLogger(UserAuthService.class);
    private final SystemUserRepository systemUserRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MFATokenManager mfaTokenManager;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    private final MfaLoginSessionRepository mfaLoginSessionRepository;


    public UserAuthService(SystemUserRepository systemUserRepository,
                           RolesRepository rolesRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           MFATokenManager mfaTokenManager,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils,
                           MfaLoginSessionRepository mfaLoginSessionRepository) {
        this.systemUserRepository = systemUserRepository;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mfaTokenManager = mfaTokenManager;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.mfaLoginSessionRepository = mfaLoginSessionRepository;
    }

    public void simpleRegister(SignupRequest signupRequest) throws Exception {
        if (systemUserRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new Exception("User with this username exists");
        }

        Roles simpleUserRole = new Roles();
        simpleUserRole.setName(AuthoritiesConstants.USER);

        SystemUser user = new SystemUser();
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        user.setUsername(signupRequest.getUsername());
        user.setAuthorities(rolesRepository.findAllByName("ROLE_USER").stream().collect(Collectors.toSet()));
        user.setSecret(mfaTokenManager.generateSecretKey());
        systemUserRepository.save(user);

    }

    public void register2FA(SignupRequest signupRequest) throws Exception {
        if (systemUserRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new Exception("User with this username exists");
        }
        Roles simpleUserRole = new Roles();
        simpleUserRole.setName(AuthoritiesConstants.USER);

        SystemUser user = new SystemUser();
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        user.setUsername(signupRequest.getUsername());
        user.setAuthorities(rolesRepository.findAllByName("ROLE_USER").stream().collect(Collectors.toSet()));
        user.setTwoFAisEnabled(Boolean.TRUE);
        user.setSecret(mfaTokenManager.generateSecretKey());
        systemUserRepository.save(user);
    }

    public byte[] mfaAccountSetup(String username) throws Exception {
        SystemUser user = systemUserRepository.findByUsername(username).get();
        if (!nonNull(user)) {
            throw new Exception("Unable to find user with this username");
        }
        if (!user.isTwoFAisEnabled()) {
            throw new Exception("2FA is not enabled for this account");
        }
        MfaTokenData token = new MfaTokenData(mfaTokenManager.getQRCode(user.getSecret()), user.getSecret());
        System.out.println("Mfa code :" + token.getMfaCode());

        String base64Image = token.getQrCode().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        return imageBytes;
    }

    public JwtToken login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        if (systemUserRepository.findByUsername(loginRequest.getUsername()).get().isTwoFAisEnabled()) {
//            mfaLoginSessionRepository.save(new MfaLoginSession(loginRequest.getUsername())).getSessionKey();
//            return "SessionKey : " + mfaLoginSessionRepository.save(new MfaLoginSession(loginRequest.getUsername())).getSessionKey();
//        }

        SystemUser userDetails = (SystemUser) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);
        log.debug("id_token is generated");
        return new JwtToken(jwt);

    }

    public JwtToken verify(String sessionKey, String code) throws Exception {

        MfaLoginSession currentSession = mfaLoginSessionRepository.findBySessionKey(sessionKey);

        if (isNull(currentSession)) {
            throw new RuntimeException("must login / redirect to login page");
        }

        if (Duration.between(currentSession.getCreatedDate(), Instant.now()).getSeconds() > 30) {
            throw new RuntimeException("login session expired / login again");
        }

        SystemUser currentUser = systemUserRepository.findByUsername(currentSession.getUsername()).get();

        mfaLoginSessionRepository.deleteById(currentSession.getId());

        if (!mfaTokenManager.verifyTotp(code, currentUser.getSecret())) {
            throw new RuntimeException("wrong code / unable to auth");
        }

        String jwt = jwtUtils.generateJwtToken(currentUser);
        return new JwtToken(jwt);

    }

}
