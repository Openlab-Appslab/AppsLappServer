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
public class Labmaster extends org.appslapp.AppsLappServer.business.pojo.users.entity.Entity {
    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Lab lab;
}
