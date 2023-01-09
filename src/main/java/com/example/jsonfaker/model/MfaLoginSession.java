package com.example.jsonfaker.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="mfa_login_session")
public class MfaLoginSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String username;

    private String sessionKey = UUID.randomUUID().toString();

    private Instant createdDate = Instant.now();

    public MfaLoginSession(String username) {
        this.username = username;
    }

    public MfaLoginSession() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
}
