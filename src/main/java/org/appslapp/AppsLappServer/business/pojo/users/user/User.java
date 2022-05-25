package org.appslapp.AppsLappServer.business.pojo.users.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "user_data")
@Getter
@Setter
@NoArgsConstructor
public class User extends org.appslapp.AppsLappServer.business.pojo.users.entity.Entity {
    @JsonIgnore
    private String verificationCode;

    private int score;

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private List<String> finishedExercises;
}
