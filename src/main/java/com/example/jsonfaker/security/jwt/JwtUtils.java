package com.example.jsonfaker.security.jwt;

import com.example.jsonfaker.configuration.JWTProperties;
import com.example.jsonfaker.model.SystemUser;
import com.example.jsonfaker.repository.SystemUserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final String AUTHORITIES_KEY = "auth";
    private final Logger logger;
    private final JWTProperties jwtProperties;
    private final SystemUserRepository systemUserRepository;

    public JwtUtils(Logger logger, JWTProperties jwtProperties, SystemUserRepository systemUserRepository) {
        this.logger = logger;
        this.jwtProperties = jwtProperties;
        this.systemUserRepository = systemUserRepository;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtProperties.getJwtCookie());
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String generateJwtToken(SystemUser userPrincipal) { // as param class which implements UserDetails
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return jwt;
    }

    public String generateTokenFromUsername(String username) {
        SystemUser currentUser = systemUserRepository.findByUsername(username).stream().findFirst().get();
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .setId(String.valueOf(currentUser.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtProperties.getJwtExpirationMs()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getJwtSecret())
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getJwtSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getJwtSecret()).parseClaimsJws(token).getBody().getSubject();
    }


}
