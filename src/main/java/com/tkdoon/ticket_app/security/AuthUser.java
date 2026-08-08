package com.tkdoon.ticket_app.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {
    private final int id;
    private final String role;

    public AuthUser(int id, String email, String role,
                    Collection<? extends GrantedAuthority> authorities) {
        super(email, "", authorities);
        this.id = id;
        this.role = role;
    }

    public String getEmail() {
        return getUsername();
    }
}
