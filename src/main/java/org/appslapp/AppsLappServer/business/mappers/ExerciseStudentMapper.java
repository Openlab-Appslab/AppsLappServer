package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.ExerciseStudentDto;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;

public class ExerciseStudentMapper {
    public static ExerciseStudentDto map(Exercise exerciseStudent) {
        ExerciseStudentDto exerciseStudentDto = new ExerciseStudentDto();
        exerciseStudentDto.setName(exerciseStudent.getName());
        exerciseStudentDto.setDescription(exerciseStudent.getDescription());
        exerciseStudentDto.setRequiredStars(exerciseStudent.getRequiredStars());
        exerciseStudentDto.setGroupName(exerciseStudent.getGroupOfExercises().getName());
        exerciseStudentDto.setId(exerciseStudent.getId());
        return exerciseStudentDto;
    }
}