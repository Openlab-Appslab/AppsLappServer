package org.appslapp.AppsLappServer.business.pojo.users.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.Exercise;
import org.appslapp.AppsLappServer.business.pojo.Lab;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity(name = "user_data")
@Getter
@Setter
@NoArgsConstructor
public class User extends org.appslapp.AppsLappServer.business.pojo.users.entity.Entity {
    @JsonIgnore
    private String verificationCode;

    private int score;

    @JsonIgnore
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Exercise> doneExercises;

    @JsonIgnore
    @ElementCollection
    private List<Long> hintedExercises;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lab_id")
    private Lab lab;
}
