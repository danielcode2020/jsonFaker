package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Roles;
import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.LoginRequest;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.model.dto.TokenResponse;
import com.example.jsonfaker.repository.RolesRepository;
import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.security.AuthoritiesConstants;
import com.example.jsonfaker.security.jwt.JwtUtils;
import com.example.jsonfaker.twoFA.MFATokenManager;
import com.example.jsonfaker.twoFA.MfaTokenData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class UserAuthService {
    private final SystemUserRepository systemUserRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MFATokenManager mfaTokenManager;
    private final AuthenticationManager authenticationManager;
    private final LoginUserService loginUserService;
    private final JwtUtils jwtUtils;


    public UserAuthService(SystemUserRepository systemUserRepository, RolesRepository rolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MFATokenManager mfaTokenManager, AuthenticationManager authenticationManager, LoginUserService loginUserService, JwtUtils jwtUtils) {
        this.systemUserRepository = systemUserRepository;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mfaTokenManager = mfaTokenManager;
        this.authenticationManager = authenticationManager;
        this.loginUserService = loginUserService;
        this.jwtUtils = jwtUtils;
    }

    public void simpleRegister(SignupRequest signupRequest) throws Exception {
        if(systemUserRepository.findByUsername(signupRequest.getUsername()).isPresent()){
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
        if(systemUserRepository.findByUsername(signupRequest.getUsername()).isPresent()){
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
        if (!nonNull(user)){
            throw new Exception("Unable to find user with this username");
        }
        if(!user.isTwoFAisEnabled()){
            throw new Exception("2FA is not enabled for this account");
        }
        MfaTokenData token =  new MfaTokenData(mfaTokenManager.getQRCode(user.getSecret()), user.getSecret());
        System.out.println("Mfa code :" +token.getMfaCode());

        String base64Image = token.getQrCode().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        return imageBytes;
    }

    public String login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(systemUserRepository.findByUsername(loginRequest.getUsername()).get().isTwoFAisEnabled()){
            return "redirect to verify page";
        }

        SystemUser userDetails = (SystemUser) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        return new TokenResponse(jwt).getToken();

    }

    public String verify(String username, String code) throws Exception {

        SystemUser user = systemUserRepository.findByUsername(username).get();
        if (!nonNull(user)){
            throw new Exception("Unable to find user with this username");
        }

        if (!mfaTokenManager.verifyTotp(code, user.getSecret())){
            return "unable to auth";
        }
        String jwt = jwtUtils.generateJwtToken(user);
        return new TokenResponse(jwt).getToken();

    }

}
