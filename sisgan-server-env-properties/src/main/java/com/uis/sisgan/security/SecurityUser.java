package com.uis.sisgan.security;

import java.util.Collection;

import com.uis.sisgan.persistence.entity.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Esta clase es la abstracción de seguridad que necesita un usuario para ser 
 * procesado en la cadena de filtros.
 *
 * @author Anderson Acuña
 */
public class SecurityUser implements UserDetails {

    private User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
