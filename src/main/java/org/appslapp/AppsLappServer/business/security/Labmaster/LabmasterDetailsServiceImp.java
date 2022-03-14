package org.appslapp.AppsLappServer.business.security.Labmaster;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LabmasterDetailsServiceImp implements UserDetailsService {
    private final LabmasterService service;

    public LabmasterDetailsServiceImp(@Autowired LabmasterService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = service.getByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + username);
        }

        return new LabmasterDetailsImp(user.get());
    }
}
