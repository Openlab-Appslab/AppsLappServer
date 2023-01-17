package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.ExerciseDto;
import org.appslapp.AppsLappServer.business.pojo.Exercise;

public class ExerciseMapper {
    public static ExerciseDto map(Exercise exercise) {
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setName(exercise.getName());
        exerciseDto.setDescription(exercise.getDescription());
        exerciseDto.setRequiredStars(exercise.getRequiredStars());
        exerciseDto.setGroupName(exercise.getGroupOfExercises().getName());
        exerciseDto.setId(exercise.getId());
        return exerciseDto;
    }
}
