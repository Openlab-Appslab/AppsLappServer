package org.appslapp.AppsLappServer.business.security.users.labmaster;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LabmasterDetailsServiceImp extends EntityDetailsServiceImp<Labmaster, LabmasterService>
        implements UserDetailsService {
    public LabmasterDetailsServiceImp(@Autowired LabmasterService service) {
        super(service);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new LabmasterDetailsImp(getUserByUsername(username));
    }
}
