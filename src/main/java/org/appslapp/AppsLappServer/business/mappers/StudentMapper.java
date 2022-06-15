package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.StudentDto;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;

import java.util.ArrayList;

public class StudentMapper {
    public static StudentDto map(User student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getUsername());
        studentDto.setExercises(new ArrayList<>());
        for (var i : student.getLab().getGroupOfExercises()) {
            for (var j : i.getExercises()) {
                studentDto.getExercises().add(ExerciseStudentMapper.map(j, student.getUsername()));
            }
        }
        studentDto.setId(student.getId());
        return studentDto;
    }
}
