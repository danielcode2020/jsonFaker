package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    @Query(value = """
         select name, username from users
        """, nativeQuery = true
    )
    List<Users> findForCrosstab();
}
