package org.appslapp.AppsLappServer.business.pojo.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(value = 0)
    private int requiredStars;

    @ManyToOne
    @JoinColumn(name = "group_of_exercises_id")
    @LazyCollection(org.hibernate.annotations.LazyCollectionOption.FALSE)
    @JsonIgnore
    private GroupOfExercises groupOfExercises;

}
