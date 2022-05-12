package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Labmaster extends org.appslapp.AppsLappServer.business.pojo.users.entity.Entity {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "labmaster")
    private List<Lab> labs;
}
