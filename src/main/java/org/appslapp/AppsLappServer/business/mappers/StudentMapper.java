package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.StudentDto;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;

public class StudentMapper {
    public static StudentDto map(User student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setName(student.getUsername());
        studentDto.setExercises(student.getLab().getGroupOfExercises().stream().
                flatMap(group -> group.getExercises().stream().map(ExerciseStudentMapper::map)).
                collect(java.util.stream.Collectors.toList()));
        studentDto.setId(student.getId());
        return studentDto;
    }
}
