package com.example.jsonfaker.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ANONYMOUS_USER("ANONYMOUS_USER"),
    ADMIN("ADMIN");

    public final String name;

    Role(String name){
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return "ROLE_" + this.name;
    }

}
