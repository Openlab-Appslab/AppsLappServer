package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.appslapp.AppsLappServer.business.pojo.exercise.ExerciseService;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercisesService;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.lab.LabService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.appslapp.AppsLappServer.business.security.users.labmaster.LabmasterDetailsImp;
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
    private final GroupOfExercisesService groupOfExercisesService;
    private final LabmasterService labmasterService;

    @Autowired
    public LabController(UserService userService, LabService labService,
                         ExerciseService exerciseService,
                         GroupOfExercisesService groupOfExercisesService,
                         LabmasterService labmasterService) {
        this.userService = userService;
        this.labService = labService;
        this.exerciseService = exerciseService;
        this.groupOfExercisesService = groupOfExercisesService;
        this.labmasterService = labmasterService;
    }

    @GetMapping("getStudents")
    public List<String> getUsers() {
        return userService.getStudents();
    }

    @PostMapping("createLab")
    public long createLab(@RequestBody Lab lab,
                          @AuthenticationPrincipal EntityDetailsImp<Labmaster> user) {
        return labService.createLab(lab, labmasterService, user.getUsername());
    }

    @GetMapping("getLabs")
    public List<Lab> getLab(@AuthenticationPrincipal EntityDetailsImp<Labmaster> user) {
        return user.getUser().getLabs();
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
