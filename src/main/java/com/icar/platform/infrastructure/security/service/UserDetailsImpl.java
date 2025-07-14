package com.icar.platform.infrastructure.security.service;

import com.icar.platform.domain.enums.UserStatus;
import com.icar.platform.domain.model.customer.Customer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final UUID id;
    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Customer customer) {
        this.id = customer.getId();
        this.username = customer.getEmail();
        this.password = customer.getPassword();
        this.isEnabled = customer.getStatus() == UserStatus.ACTIVE;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return this.isEnabled;
    }
}