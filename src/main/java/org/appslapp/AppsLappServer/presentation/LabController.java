package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/management/")
public class LabController {

    private final UserService userService;

    public LabController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getStudents")
    public List<String> getUsers() {
        return userService.getStudents();
    }
}
