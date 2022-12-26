package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findAllByName(String name);

}
