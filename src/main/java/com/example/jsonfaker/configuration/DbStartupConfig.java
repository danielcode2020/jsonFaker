package com.example.jsonfaker.configuration;

import com.example.jsonfaker.enums.Role;
import com.example.jsonfaker.model.Address;
import com.example.jsonfaker.model.Company;
import com.example.jsonfaker.model.Geo;
import com.example.jsonfaker.model.Users;
import com.example.jsonfaker.repository.UsersRepository;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DbStartupConfig {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger logger;

    public DbStartupConfig(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Logger logger) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.logger = logger;
    }

    @PostConstruct
    private void addAdminToDb(){
        Geo geoAdmin = new Geo(2.3, 3.4);
        Address addressAdmin = new Address("streetx","suitex","cityx", "zipcodex",geoAdmin);
        Company companyAdmin = new Company("esempla","catchpx","bsx");
        Users admin = new Users("admin","admin",bCryptPasswordEncoder.encode("admin"), Role.ADMIN,"admin@gmail.com","1234","ww.rand",addressAdmin,companyAdmin);
        usersRepository.save(admin);
        logger.info("admin added to db");
    }
}
