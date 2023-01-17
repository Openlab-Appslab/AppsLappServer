package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.ExerciseStudentDto;
import org.appslapp.AppsLappServer.business.pojo.Exercise;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;

public class ExerciseStudentMapper {
    public static ExerciseStudentDto map(Exercise exerciseStudent, User user) {
        ExerciseStudentDto exerciseStudentDto = new ExerciseStudentDto();
        exerciseStudentDto.setName(exerciseStudent.getName());
        exerciseStudentDto.setDescription(exerciseStudent.getDescription());
        exerciseStudentDto.setRequiredStars(exerciseStudent.getRequiredStars());
        exerciseStudentDto.setGroupName(exerciseStudent.getGroupOfExercises().getName());
        exerciseStudentDto.setId(exerciseStudent.getId());
        exerciseStudentDto.setDone(user.getDoneExercises().contains(exerciseStudent));
        return exerciseStudentDto;
    }
}
