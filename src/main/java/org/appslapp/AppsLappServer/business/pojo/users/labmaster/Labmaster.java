package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Labmaster extends org.appslapp.AppsLappServer.business.pojo.users.entity.Entity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "labmaster")
    private List<Lab> labs;
}
