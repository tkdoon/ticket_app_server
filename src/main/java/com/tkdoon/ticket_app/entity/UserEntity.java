package com.tkdoon.ticket_app.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class UserEntity {
    private String email;
    private String role;
    private String userName;
    private int id;
    private List<GrantedAuthority> authorities=new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")
            ));

    public void checkAdmin() {
        if (Objects.equals(role, "admin")) {
            List<GrantedAuthority> admin_auth = new ArrayList<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN")));
            setAuthorities(admin_auth);
    }}
}
