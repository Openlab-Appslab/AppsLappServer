package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.ExerciseDto;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;

public class ExerciseMapper {
    public static ExerciseDto map(Exercise exercise) {
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setName(exercise.getName());
        exerciseDto.setDescription(exercise.getDescription());
        exerciseDto.setMinStars(exercise.getMinStars());
        exerciseDto.setMaxStars(exercise.getMaxStars());
        exerciseDto.setGroupName(exercise.getGroupOfExercises().getName());
        return exerciseDto;
    }
}
