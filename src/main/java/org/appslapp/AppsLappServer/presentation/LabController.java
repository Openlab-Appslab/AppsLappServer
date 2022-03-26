package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.appslapp.AppsLappServer.business.pojo.exercise.ExerciseService;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercisesService;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.lab.LabService;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.appslapp.AppsLappServer.business.security.Labmaster.LabmasterDetailsImp;
import org.appslapp.AppsLappServer.business.security.User.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/management/")
public class LabController {

    private final UserService userService;
    private final LabService labService;
    private final ExerciseService exerciseService;
    private final GroupOfExercisesService groupOfExercisesService;

    public LabController(@Autowired UserService userService, @Autowired LabService labService,
                         @Autowired ExerciseService exerciseService,
                         @Autowired GroupOfExercisesService groupOfExercisesService) {
        this.userService = userService;
        this.labService = labService;
        this.exerciseService = exerciseService;
        this.groupOfExercisesService = groupOfExercisesService;
    }

    @GetMapping("getStudents")
    public List<String> getUsers() {
        return userService.getStudents();
    }

    @PostMapping("createLab")
    public long createLab(@RequestBody Map<String, String> test, @AuthenticationPrincipal UserDetailsImp user) {
        Lab lab = new Lab();
        lab.setName(test.get("name"));
        lab.setStudentNames(List.of(test.get(("studentNames")).split(",,,")));
        System.out.println(lab.getStudentNames());
        return labService.save(lab);
    }

    @GetMapping("getLab")
    public Lab getLab(@AuthenticationPrincipal LabmasterDetailsImp user) {
        return user.getLab();
    }

    @PostMapping("createExercise")
    public Long createExercise(@Valid @RequestBody Exercise exercise) {
        return exerciseService.save(exercise);
    }

    @GetMapping("getAllExercises")
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @PostMapping("createGroupOfExercises")
    public Long createGroupOfExercises(@Valid @RequestBody GroupOfExercises groupOfExercises) {
        return groupOfExercisesService.save(groupOfExercises);
    }
}
