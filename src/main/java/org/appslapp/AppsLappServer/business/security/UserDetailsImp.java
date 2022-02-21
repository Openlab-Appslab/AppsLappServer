package org.appslapp.AppsLappServer.business.security;

import org.appslapp.AppsLappServer.business.pojo.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements UserDetails {
    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;
    private final String firstName;
    private final String lastName;
    private final boolean isEnabled;
    private final long id;

    public UserDetailsImp(User user) {
        username = user.getUsername();
        password = user.getPassword();
        rolesAndAuthorities = List.of(new SimpleGrantedAuthority(user.getAuthority()));
        isEnabled = user.isEnabled();
        id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
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
        return isEnabled;
    }
}
