package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.StudentDto;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;

import java.util.ArrayList;

public class StudentMapper {
    public static StudentDto map(User student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getUsername());
        studentDto.setExercises(new ArrayList<>());
        studentDto.setAwards(new ArrayList<>());

        var score = 0;
        for (var i : student.getLab().getGroupOfExercises()) {
            var done = 0;
            for (var j : i.getExercises()) {
                var exercise = ExerciseStudentMapper.map(j, student);
                if (exercise.isDone()) {
                    score += exercise.getRequiredStars();
                    done++;
                }
                studentDto.getExercises().add(exercise);
            }

            if (done == i.getExercises().size()) {
                studentDto.getAwards().add(i.getAward());
            }
        }
        studentDto.setId(student.getId());
        studentDto.setScore(score);
        return studentDto;
    }
}
