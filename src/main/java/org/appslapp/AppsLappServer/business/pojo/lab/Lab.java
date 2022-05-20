package org.appslapp.AppsLappServer.business.pojo.lab;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "labmaster_id", nullable = false)
    private Labmaster labmaster;

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> studentNames;

    @NotBlank
    private String name;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GroupOfExercises> groupOfExercises;
}
