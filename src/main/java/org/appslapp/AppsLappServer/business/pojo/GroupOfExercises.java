package org.appslapp.AppsLappServer.business.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    private String award;

    private String deadline;

    private int minStars;

    private int maxStars;

    private boolean enabled;

    @OneToMany(mappedBy = "groupOfExercises")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Exercise> exercises;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    @JsonIgnore
    private Lab lab;
}
