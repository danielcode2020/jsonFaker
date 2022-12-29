package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Roles;
import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.repository.RolesRepository;
import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.security.AuthoritiesConstants;
import com.example.jsonfaker.twoFA.MFATokenManager;
import com.example.jsonfaker.twoFA.MfaTokenData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class UserAuthService {
    private final SystemUserRepository systemUserRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MFATokenManager mfaTokenManager;

    public UserAuthService(SystemUserRepository systemUserRepository, RolesRepository rolesRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MFATokenManager mfaTokenManager) {
        this.systemUserRepository = systemUserRepository;
        this.rolesRepository = rolesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mfaTokenManager = mfaTokenManager;
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

        String base64Image = token.getQrCode().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        return imageBytes;
    }

//    public String verify(String username, String code){
//
//    }
}
