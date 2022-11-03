package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}
