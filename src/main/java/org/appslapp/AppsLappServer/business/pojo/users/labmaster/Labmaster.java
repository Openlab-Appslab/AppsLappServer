package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Labmaster {
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

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Lab lab;
}
