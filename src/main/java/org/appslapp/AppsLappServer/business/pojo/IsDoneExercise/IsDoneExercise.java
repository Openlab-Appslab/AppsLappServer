package org.appslapp.AppsLappServer.business.pojo.IsDoneExercise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class IsDoneExercise {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    private Long id;

    private boolean isDone;
    private String userName;

    public IsDoneExercise(boolean isDone, String userName) {
        this.isDone = isDone;
        this.userName = userName;
    }
}
