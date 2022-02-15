package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.security.UserDetailsImp;
import org.appslapp.AppsLappServer.business.security.user.User;
import org.appslapp.AppsLappServer.business.security.user.UserService;
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
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin("*")
    @PostMapping("/api/auth/register")
    public ResponseEntity<Long> register(@Valid @RequestBody User user) {
        var id = userService.save(user);
        System.out.println("Custom logs:" + id);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");

        if (id == -2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @CrossOrigin("*")
    @GetMapping("/api/auth/login")
    public long login(@AuthenticationPrincipal UserDetailsImp details) {
        return details.getId();
    }

    @CrossOrigin("*")
    @PostMapping("/api/auth/resendEmail/{username}")
    public long resend(@PathVariable String username) {
        var id = userService.resendEmail(username);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, username);

        return id;
    }

    @GetMapping("/api/test")
    public int testApiLimiter() {
        return 2;
    }

    @GetMapping("/api/secureTest")
    public int testSecurity() {
        return 3;
    }

    @GetMapping("/api/verify")
    public ResponseEntity<Void> verifyEmail(@RequestParam String code) {
        var user = userService.findByCode(code);

        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        var u = user.get();
        u.setEnabled(true);
        userService.update(u);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://appslappapp.vercel.app/emailV?email=" + user.get().getEmail())).build();
    }
}
