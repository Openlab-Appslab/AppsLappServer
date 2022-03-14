package org.appslapp.AppsLappServer.business.security.Labmaster;

import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class LabmasterDetailsImp implements UserDetails {
    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;
    private final String firstName;
    private final String lastName;
    private final long id;
    private final Lab lab;

    public LabmasterDetailsImp(Labmaster labmaster) {
        username = labmaster.getUsername();
        password = labmaster.getPassword();
        rolesAndAuthorities = List.of(new SimpleGrantedAuthority(labmaster.getAuthority()));
        id = labmaster.getId();
        this.firstName = labmaster.getFirstName();
        this.lastName = labmaster.getLastName();
        this.lab = labmaster.getLab();
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

    public Lab getLab() {
        return lab;
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
        return true;
    }
}
