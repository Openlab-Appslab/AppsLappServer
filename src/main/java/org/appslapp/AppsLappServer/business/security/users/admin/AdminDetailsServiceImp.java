package org.appslapp.AppsLappServer.business.security.users.admin;

import lombok.extern.slf4j.Slf4j;
import org.appslapp.AppsLappServer.business.pojo.users.admin.Admin;
import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminDetailsServiceImp extends EntityDetailsServiceImp<Admin, AdminService> implements UserDetailsService {
    public AdminDetailsServiceImp(@Autowired AdminService service) {
        super(service);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(username);
        return new EntityDetailsImp<>(getUserByUsername(username));
    }
}
