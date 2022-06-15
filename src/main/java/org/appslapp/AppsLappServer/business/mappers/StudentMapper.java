package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.StudentDto;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;

public class StudentMapper {
    public static StudentDto map(User student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getUsername());
        for (var i : student.getLab().getGroupOfExercises()) {
            for (var j : i.getExercises()) {
                studentDto.getExercises().add(ExerciseStudentMapper.map(j));
            }
        }
        studentDto.setId(student.getId());
        return studentDto;
    }
}
