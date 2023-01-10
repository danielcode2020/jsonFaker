package com.example.jsonfaker.repository;

import com.example.jsonfaker.model.MfaLoginSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfaLoginSessionRepository extends JpaRepository<MfaLoginSession, Long> {
    MfaLoginSession findBySessionKey(String sessionKey);
}
