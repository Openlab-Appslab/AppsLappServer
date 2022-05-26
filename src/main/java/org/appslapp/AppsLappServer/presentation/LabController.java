package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.Dto.ExerciseDto;
import org.appslapp.AppsLappServer.business.helper.CreateLabHelper;
import org.appslapp.AppsLappServer.business.helper.ExerciseUpdateHelper;
import org.appslapp.AppsLappServer.business.helper.GroupOfExercisesToLabHelper;
import org.appslapp.AppsLappServer.business.helper.ExerciseWithGroupHelper;
import org.appslapp.AppsLappServer.business.mappers.ExerciseMapper;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.appslapp.AppsLappServer.business.pojo.exercise.ExerciseService;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercisesService;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.lab.LabService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.appslapp.AppsLappServer.exceptions.GroupOfExercisesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public long createLab(@RequestBody CreateLabHelper body,
                          @AuthenticationPrincipal EntityDetailsImp<Labmaster> user) {
        var lab = new Lab();
        lab.setName(body.getName());
        lab.setStudentNames(body.getStudentNames().stream()
                .map(userService::getUserByFullName)
                .collect(Collectors.toList()));
        
        return labService.createLab(lab, labmasterService, user.getUsername());
    }

    @GetMapping("getLabs")
    public List<Lab> getLab(@AuthenticationPrincipal EntityDetailsImp<Labmaster> user) {
        return user.getUser().getLabs();
    }

    @GetMapping("getLab/{labId}")
    public Lab getLab(@PathVariable long labId) {
        return labService.getLab(labId);
    }

    @PostMapping("createExercise")
    public Long createExercise(@RequestBody ExerciseWithGroupHelper body) {
        var exercise = new Exercise();
        exercise.setDescription(body.getDescription());
        exercise.setName(body.getName());
        exercise.setMaxStars(body.getMaxStars());
        exercise.setMinStars(body.getMinStars());
        try {
            var group = groupOfExercisesService.getGroupOfExercisesByName(body.getGroupName());
            group.getExercises().add(exercise);
            exercise.setGroupOfExercises(group);
            return exerciseService.save(exercise, groupOfExercisesService);
        } catch (GroupOfExercisesNotFoundException ignored) {
            var group = new GroupOfExercises();
            group.setName(body.getGroupName());
            group.setExercises(List.of(exercise));
            exercise.setGroupOfExercises(group);
            return exerciseService.save(exercise, groupOfExercisesService);
        }
    }

    @GetMapping("getAllExercises")
    public List<ExerciseDto> getAllExercises() {;
        return exerciseService.getAllExercises().stream().map(ExerciseMapper::map).collect(Collectors.toList());
    }

    @GetMapping("getAllGroups")
    public List<GroupOfExercises> getAllGroups() {
        return groupOfExercisesService.findAll();
    }

    @PostMapping("createGroupOfExercises")
    public Long createGroupOfExercises(@Valid @RequestBody GroupOfExercises groupOfExercises) {
        return groupOfExercisesService.save(groupOfExercises);
    }

    @PostMapping("addGroupToLab")
    public Long addExerciseToLab(@RequestBody GroupOfExercisesToLabHelper body){
        var lab = labService.getLab(body.getLabId());
        var groups = body.getGroupsOfExercises().stream()
                .map(groupOfExercisesService::getGroupOfExercisesByName)
                .collect(Collectors.toList());
        lab.getGroupOfExercises().addAll(groups);
        labService.save(lab);
        return 1L;
    }

    @GetMapping("getStudent/{studentId}")
    public User getStudent(@PathVariable Long studentId) {
        return userService.getUserById(studentId);
    }

     @PostMapping("updateScore")
     public Long updateScore(@RequestBody ExerciseUpdateHelper body) {
        var user = userService.getUserById(body.getStudentId());
        user.setScore(user.getScore() + body.getScore());
        user.getFinishedExercises().add(body.getExerciseName());
        userService.update(user);
        return 1L;
     }

}
