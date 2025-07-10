package ru.vldaislab.bekrenev.authservice.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN, ANALYST, USER;


    @Override
    public String getAuthority() {
        return name();
    }
}
