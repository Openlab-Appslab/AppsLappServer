package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.Dto.StudentDto;
import org.appslapp.AppsLappServer.business.mappers.StudentMapper;
import org.appslapp.AppsLappServer.business.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/")
public class StudentController {

    private final UserService userService;

    @Autowired
    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<String> getUsers() {
        return userService.getStudents();
    }

    @GetMapping("{studentId}")
    public StudentDto getStudent(@PathVariable Long studentId) {
        var student = userService.getUserById(studentId);
        return StudentMapper.map(student);
    }
}
