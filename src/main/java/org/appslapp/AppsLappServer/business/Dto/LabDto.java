package org.appslapp.AppsLappServer.business.Dto;

import lombok.Getter;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.pojo.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import java.util.List;

@Getter
@Setter
public class LabDto {
    private long id;

    private Labmaster labmaster;

    private List<StudentDtoNoScore> studentNames;

    private String name;

    private List<GroupOfExercisesDto> groupOfExercises;
}
