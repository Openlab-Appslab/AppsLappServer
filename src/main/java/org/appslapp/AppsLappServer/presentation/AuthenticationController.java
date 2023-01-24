package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.helper.ResetPasswordHelper;
import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.appslapp.AppsLappServer.business.services.AdminService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.services.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    private final UserService userService;
    private final AdminService adminService;
    private final LabmasterService labmasterService;

    @Autowired
    public AuthenticationController(UserService userService, AdminService adminService,
                                    LabmasterService labmasterService) {
        this.userService = userService;
        this.adminService = adminService;
        this.labmasterService = labmasterService;
    }

    @PostMapping("resetPassword/{username}")
    public long resetPassword(@PathVariable String username) {
        try {
            return userService.resetPasswordEmail(username);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @PostMapping("resetPassword")
    public long resetPassword(@Valid @RequestBody ResetPasswordHelper resetPasswordHelper) {
        return userService.resetPassword(resetPasswordHelper.getUsername(), resetPasswordHelper.getPassword());
    }

    @PostMapping("register")
    public long register(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("login")
    public String login(Authentication principal) {
        return principal.getAuthorities().toString();
    }

    @PostMapping("resendEmail/{username}")
    public long resendEmail(@PathVariable String username) {
        return userService.resendEmail(username);
    }

    @GetMapping("verify/{code}")
    public ResponseEntity<Void> verifyEmail(@PathVariable String code) {
        userService.verifyUser(code);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT ).location(
                URI.create("https://apps-lapp-web.vercel.app/emailV")).build();
    }

    @PostMapping("promoteToLabmaster")
    public long promoteToLabmaster(@RequestParam String username, @RequestParam String password) {
        return labmasterService.save(userService.createLabmaster(username, password));
    }

    //Only for test purposes
    @PostMapping("createAdmins")
    public void tem() {
        /*var admin = new Admin();
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
        userService.save(user);*/

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
