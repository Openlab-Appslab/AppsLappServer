package org.appslapp.AppsLappServer.business.pojo.lab;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "labmaster_id", nullable = false)
    private Labmaster labmaster;

    @ElementCollection(fetch = FetchType.EAGER) //Spytat sa martina ci neni better solution lebo toto je HUGE performance hit
    private List<String> studentNames;

    @NotBlank
    private String name;
}
