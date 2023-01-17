package org.appslapp.AppsLappServer.business.pojo.users.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.Exercise;
import org.appslapp.AppsLappServer.business.pojo.Lab;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
    @ManyToOne
    @JoinColumn(name = "lab_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Lab lab;
}
