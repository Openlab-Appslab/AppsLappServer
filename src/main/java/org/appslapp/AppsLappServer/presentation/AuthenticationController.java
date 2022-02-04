package org.appslapp.AppsLappServer.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @PostMapping("/api/auth/register")
    public int register() {
        return 0;
    }
}
