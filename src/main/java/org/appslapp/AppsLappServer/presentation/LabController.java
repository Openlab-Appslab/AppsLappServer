package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.appslapp.AppsLappServer.business.pojo.exercise.ExerciseService;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.lab.LabService;
import org.appslapp.AppsLappServer.business.pojo.user.UserService;
import org.appslapp.AppsLappServer.business.security.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/management/")
public class LabController {

    private final UserService userService;
    private final LabService labService;
    private final ExerciseService exerciseService;

    public LabController(@Autowired UserService userService, @Autowired LabService labService,
                         @Autowired ExerciseService exerciseService) {
        this.userService = userService;
        this.labService = labService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("getStudents")
    public List<String> getUsers() {
        return userService.getStudents();
    }

    @CrossOrigin("*")
    @PostMapping("createLab")
    public long createLab(@RequestBody Map<String, String> test, @AuthenticationPrincipal UserDetailsImp user) {
        Lab lab = new Lab();
        lab.setName(test.get("name"));
        lab.setStudentNames(List.of(test.get(("studentNames")).split(",,,")));
        System.out.println(lab.getStudentNames());
        lab.setLabMasterId(user.getId());
        return labService.save(lab);
    }

    @CrossOrigin("*")
    @GetMapping("getLabs")
    public List<Lab> getLabs(@AuthenticationPrincipal UserDetailsImp user) {
        return labService.findAllByLabmasterId(user.getId());
    }

    @CrossOrigin("*")
    @PostMapping("createExercise")
    public Long createExercise(@Valid @RequestBody Exercise exercise) {
        return exerciseService.save(exercise);
    }

    @GetMapping("getAllExercises")
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }
}
