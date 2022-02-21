package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.security.UserDetailsImp;
import org.appslapp.AppsLappServer.business.pojo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("get")
    public Map<String, String> getRegisteredUser(@AuthenticationPrincipal UserDetailsImp principal) {
        return Map.of("firstName", principal.getFirstName(),
                "lastName", principal.getLastName());
    }


}
