package com.example.jsonfaker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
public class JWTProperties {
    @NotEmpty
    private String jwtSecret;

    @NotEmpty
    private int jwtExpirationMs;

    @NotEmpty
    private String jwtCookie;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public void setJwtExpirationMs(int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String getJwtCookie() {
        return jwtCookie;
    }

    public void setJwtCookie(String jwtCookie) {
        this.jwtCookie = jwtCookie;
    }
}
