package org.appslapp.AppsLappServer.business.pojo.users.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
public class Entity {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private String email;
    private boolean enabled;
    private long id;

    public GrantedAuthority getGrantedAuthority() {
        return new SimpleGrantedAuthority(role);
    }
}
