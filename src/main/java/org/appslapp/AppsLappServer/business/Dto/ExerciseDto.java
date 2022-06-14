package org.appslapp.AppsLappServer.business.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDto {
    String name;
    String description;
    int requiredStars;
    String groupName;
    Long id;
}
