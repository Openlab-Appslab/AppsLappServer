package org.appslapp.AppsLappServer.business.pojo.groupOfExercises;

import lombok.Getter;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
public class GroupOfExercises {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "groupOfExercises")
    private List<Exercise> exercises;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lab_id")
    private Lab lab;

    private String description;
}
