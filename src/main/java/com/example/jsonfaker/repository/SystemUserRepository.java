package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.SystemUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends CrudRepository<SystemUser, Long> {
    Optional<SystemUser> findByUsername(String username);
}
