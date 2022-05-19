package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @GetMapping("get")
    public Map<String, String> getRegisteredUser(@AuthenticationPrincipal EntityDetailsImp<? extends Entity> principal) {
        return Map.of("firstName", principal.getFirstName(),
                "lastName", principal.getLastName());
    }
}
