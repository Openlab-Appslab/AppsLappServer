package org.appslapp.AppsLappServer.business.pojo.lab;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "labmaster_id", nullable = false)
    private Labmaster labmaster;

    @OneToMany
    @JoinColumn(name = "lab_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> studentNames;

    @NotBlank
    private String name;

    @OneToMany
    @JoinColumn(name = "lab_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GroupOfExercises> groupOfExercises;
}
