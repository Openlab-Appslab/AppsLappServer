package org.appslapp.AppsLappServer.business.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDto {
    private String name;
    private String description;
    private int requiredStars;
    private String groupName;
    private Long id;
}
