package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.lab.LabService;
import org.appslapp.AppsLappServer.business.pojo.user.UserService;
import org.appslapp.AppsLappServer.business.security.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/management/")
public class LabController {

    private final UserService userService;
    private final LabService labService;

    public LabController(@Autowired UserService userService, @Autowired LabService labService) {
        this.userService = userService;
        this.labService = labService;
    }

    @GetMapping("getStudents")
    public List<String> getUsers() {
        return userService.getStudents();
    }

    @PostMapping("createLab")
    public long createLab(@Valid @RequestBody Lab lab, @AuthenticationPrincipal UserDetailsImp user) {
        lab.setLabMasterId(user.getId());
        return labService.save(lab);
    }

    @GetMapping("getLabs")
    public List<Lab> getLabs(@AuthenticationPrincipal UserDetailsImp user) {
        return labService.findAllByLabmasterId(user.getId());
    }
}
