package org.appslapp.AppsLappServer.business.security.Admin;

import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsServiceImp implements UserDetailsService {
    private final AdminService service;

    public AdminDetailsServiceImp(@Autowired AdminService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = service.getUserByName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        return new AdminDetailsImp(user.get());
    }
}
