package org.appslapp.AppsLappServer.business.pojo.users.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity(name = "user_data")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "[a-zA-Z0-9._]{4,10}", message = "Name contains illegal characters or is too long.")
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;

    @JsonIgnore
    private String authority;

    @JsonIgnore
    private boolean enabled;

    @JsonIgnore
    private String verificationCode;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
