package org.appslapp.AppsLappServer.business.pojo.groupOfExercises;

import lombok.Getter;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
}
