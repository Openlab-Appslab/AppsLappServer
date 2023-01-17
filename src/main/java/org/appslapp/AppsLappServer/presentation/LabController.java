package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.helper.CreateLabHelper;
import org.appslapp.AppsLappServer.business.helper.GroupOfExercisesToLabHelper;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercisesService;
import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.lab.LabService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lab/")
public class LabController {

    private final UserService userService;
    private final LabService labService;
    private final GroupOfExercisesService groupOfExercisesService;
    private final LabmasterService labmasterService;

    @Autowired
    public LabController(UserService userService, LabService labService,
                         GroupOfExercisesService groupOfExercisesService,
                         LabmasterService labmasterService) {
        this.userService = userService;
        this.labService = labService;
        this.groupOfExercisesService = groupOfExercisesService;
        this.labmasterService = labmasterService;
    }

    @PostMapping
    public long createLab(@RequestBody CreateLabHelper body,
                          @AuthenticationPrincipal EntityDetailsImp<Labmaster> user) {
        var lab = new Lab();
        lab.setName(body.getName());
        lab.setStudentNames(body.getStudentNames().stream()
                .map(userService::getUserByFullName)
                .collect(Collectors.toList()));
        
        return labService.createLab(lab, labmasterService, user.getUsername());
    }

    @GetMapping("labmaster")
    public List<Lab> getLab(@AuthenticationPrincipal EntityDetailsImp<Labmaster> user) {
        return user.getUser().getLabs();
    }

    @GetMapping("{labId}")
    public Lab getLab(@PathVariable long labId) {
        return labService.getLab(labId);
    }

    @GetMapping("getAllGroups")
    public List<GroupOfExercises> getAllGroups() {
        return groupOfExercisesService.findAll();
    }

    @PostMapping("createGroupOfExercises")
    public Long createGroupOfExercises(@Valid @RequestBody GroupOfExercises groupOfExercises) {
        return groupOfExercisesService.save(groupOfExercises);
    }

    @PostMapping("{labId}/addGroup")
    public Long addExerciseToLab(@PathVariable long labId, @RequestBody GroupOfExercisesToLabHelper body){
        var lab = labService.getLab(labId);
        var groups = body.getGroupsOfExercises().stream()
                .map(groupOfExercisesService::getGroupOfExercisesByName)
                .collect(Collectors.toList());
        lab.getGroupOfExercises().addAll(groups);
        labService.save(lab);
        return 1L;
    }

     @GetMapping
     public Lab getLab(@AuthenticationPrincipal User user) {
        return user.getLab();
     }
}
