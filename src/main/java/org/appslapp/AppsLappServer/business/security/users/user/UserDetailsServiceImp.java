package org.appslapp.AppsLappServer.business.security.users.user;

import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp extends EntityDetailsServiceImp<User, UserService> implements UserDetailsService {
    public UserDetailsServiceImp(@Autowired UserService service) {
        super(service);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new EntityDetailsImp<User>(getUserByUsername(username));
    }
}
