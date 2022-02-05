package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.security.user.User;
import org.appslapp.AppsLappServer.business.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/register")
    public long register(@Valid @RequestBody User user) {
        var id = userService.save(user);

        if (id == -1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");

        if (id == -2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists.");

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
    public long verifyEmail(@RequestParam String code) {
        var user = userService.findByCode(code);

        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "User doesn't exist");

        return userService.enable(user.get());
    }
}
