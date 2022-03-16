package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.users.admin.Admin;
import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.appslapp.AppsLappServer.business.security.User.UserDetailsImp;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final UserService userService;
    private final AdminService adminService;

    public AuthenticationController(@Autowired UserService userService, @Autowired AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
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
    public long login(@AuthenticationPrincipal UserDetailsImp details) {
        return details.getId();
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

    @PostMapping("createAdmins")
    public void tem() {
        var filip = new Admin();
        filip.setFirstName("Filip");
        filip.setLastName("David");
        filip.setUsername("Filipko");

        adminService.save(filip);

        var kubo = new Admin();
        filip.setFirstName("Jakub");
        filip.setLastName("Kapitulcin");
        filip.setUsername("Kubino");

        adminService.save(kubo);
    }
}
