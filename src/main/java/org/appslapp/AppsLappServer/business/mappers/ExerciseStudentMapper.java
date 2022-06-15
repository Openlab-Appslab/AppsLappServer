package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.ExerciseStudentDto;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;

public class ExerciseStudentMapper {
    public static ExerciseStudentDto map(Exercise exerciseStudent, String userName) {
        ExerciseStudentDto exerciseStudentDto = new ExerciseStudentDto();
        exerciseStudentDto.setName(exerciseStudent.getName());
        exerciseStudentDto.setDescription(exerciseStudent.getDescription());
        exerciseStudentDto.setRequiredStars(exerciseStudent.getRequiredStars());
        exerciseStudentDto.setGroupName(exerciseStudent.getGroupOfExercises().getName());
        exerciseStudentDto.setId(exerciseStudent.getId());
        exerciseStudentDto.setDone(exerciseStudent.getIsDoneExercises().stream()
                .anyMatch(i -> i.getUserName().equals(userName)));
        return exerciseStudentDto;
    }
}
