package org.appslapp.AppsLappServer.business.security.users.entity;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class EntityDetailsImp<T extends Entity> implements UserDetails {
    protected  T user;

    public EntityDetailsImp(T user) {
        this.user = user;
    }

    public long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.createGrantedAuthority());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getFirstName() {
        return user.getFirstName();
    }
}
