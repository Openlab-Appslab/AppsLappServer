package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.users.admin.Admin;
import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
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
        System.out.println(labmasterService.getUserByName("skapMaster"));
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
        var admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("Heslo123_");
        admin.setFirstName("Martinko");
        admin.setLastName("Klingacik");
        admin.setEnabled(true);
        admin.setEmail("huhuhu@gmail.com");
        admin.setAuthority("ADMIN");
        adminService.save(admin);

        var user = new User();
        user.setUsername("user");
        user.setPassword("Heslo123_");
        user.setFirstName("ferko");
        user.setLastName("jozko");
        user.setEnabled(true);
        user.setEmail("emailovaadresa@gmail.com");
        user.setAuthority("PUPIL");
        userService.save(user);

        var labmaster =  new Labmaster();
        labmaster.setUsername("ratatui");
        labmaster.setPassword("Heslo123_");
        labmaster.setFirstName("jozko");
        labmaster.setLastName("ferko");
        labmaster.setEnabled(true);
        labmaster.setEmail("tratata@gmail.com");
        labmaster.setAuthority("LABMASTER");
        labmasterService.save(labmaster);
    }
}
