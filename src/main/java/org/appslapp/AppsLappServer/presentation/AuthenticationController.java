package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.users.admin.Admin;
import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final UserService userService;
    private final AdminService adminService;
    private final LabmasterService labmasterService;

    public AuthenticationController(@Autowired UserService userService, @Autowired AdminService adminService,
                                    @Autowired LabmasterService labmasterService) {
        this.userService = userService;
        this.adminService = adminService;
        this.labmasterService = labmasterService;
        System.out.println(labmasterService.getByUsername("skapMaster"));
    }

    @PostMapping("register")
    public ResponseEntity<Long> register(@Valid @RequestBody User user) {
        var id = userService.save(user);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");

        if (id == -2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("login")
    public long login(@AuthenticationPrincipal UserDetails details) {
        return 0;
    }

    @PostMapping("resendEmail/{username}")
    public long resend(@PathVariable String username) {
        var id = userService.resendEmail(username);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, username);

        return id;
    }

    @GetMapping("verify")
    public ResponseEntity<Void> verifyEmail(@RequestParam String code) {
        var user = userService.findByCode(code);

        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        var u = user.get();
        u.setEnabled(true);
        userService.update(u);

        return ResponseEntity.status(HttpStatus.FOUND).location(
                URI.create("https://appslappapp.vercel.app/emailV?email=" + user.get().getEmail())).build();
    }

    @PostMapping("promoteToLabmaster")
    public long promoteToLabmaster(@RequestParam String username, @RequestParam String password) {
        var user = userService.getUserByName(username);

        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, username);

        var labmaster = userService.createLabmaster(user.get(), password);
        return labmasterService.save(labmaster);
    }

    @PostMapping("createAdmins")
    public void tem() {
        var filip = new Admin();
        filip.setFirstName("Filip");
        filip.setLastName("David");
        filip.setUsername("Filipko");
        filip.setPassword("Heslo123_");

        adminService.save(filip);

        var kubo = new Admin();
        kubo.setFirstName("Jakub");
        kubo.setLastName("Kapitulcin");
        kubo.setUsername("Kubino");
        kubo.setPassword("Heslo123#");

        adminService.save(kubo);
    }
}
