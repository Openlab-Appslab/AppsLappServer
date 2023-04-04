package org.appslapp.AppsLappServer.business.Dto;

import lombok.Getter;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.Exercise;
import org.appslapp.AppsLappServer.business.pojo.Lab;

import java.util.List;

@Getter
@Setter
public class GroupOfExercisesDto {
    private Long id;

    private String name;

    private String award;

    private long deadline;

    private int minStars;

    private int maxStars;

    private boolean enabled;

    private List<Exercise> exercises;

    private Lab lab;
}