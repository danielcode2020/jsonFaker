package com.example.jsonfaker.service;

import com.example.jsonfaker.model.Roles;
import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.model.dto.SignupRequest;
import com.example.jsonfaker.repository.RolesRepository;
import com.example.jsonfaker.repository.SystemUserRepository;
import com.example.jsonfaker.security.AuthoritiesConstants;
import com.example.jsonfaker.twoFA.MFATokenManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
        user.setSecret(mfaTokenManager.generateSecretKey());
        systemUserRepository.save(user);
    }
}
