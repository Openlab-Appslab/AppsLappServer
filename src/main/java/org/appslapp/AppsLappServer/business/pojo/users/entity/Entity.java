package org.appslapp.AppsLappServer.business.pojo.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "[a-zA-Z0-9._]{4,10}", message = "Name contains illegal characters or is too long.")
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    @JsonIgnore
    private String authority;

    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    public GrantedAuthority createGrantedAuthority() {
        return new SimpleGrantedAuthority(authority);
    }
}
