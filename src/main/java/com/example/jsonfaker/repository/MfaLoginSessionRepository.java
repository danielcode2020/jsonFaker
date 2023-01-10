package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.MfaLoginSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MfaLoginSessionRepository extends JpaRepository<MfaLoginSession, Long> {
    MfaLoginSession findBySessionKey(String sessionKey);
}
