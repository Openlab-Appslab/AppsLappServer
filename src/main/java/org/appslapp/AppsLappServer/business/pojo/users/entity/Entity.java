package org.appslapp.AppsLappServer.business.pojo.users.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Entity {

    @Pattern(regexp = "[a-zA-Z0-9._]{4,10}", message = "Name contains illegal characters or is too long.")
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String authority;

    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;

    private boolean enabled;

    public GrantedAuthority createGrantedAuthority() {
        return new SimpleGrantedAuthority(authority);
    }
}
