package org.appslapp.AppsLappServer.business.security.users.entity;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class EntityDetailsServiceImp<T extends EntityService<? extends Entity>> implements UserDetailsService {
    private final List<T> entityService;

    public EntityDetailsServiceImp(List<T> entityService) {
        this.entityService = entityService;
    }

    @Override
    public EntityDetailsImp<? extends Entity> loadUserByUsername(String username) throws UsernameNotFoundException {
        for (var service : entityService) {
            try {
                var user =  service.getUserByName(username);
                if (user == null)
                    continue;
                return new EntityDetailsImp<>(user);
            } catch (UserNotFoundException ignored) {

            }
        }

        throw new UsernameNotFoundException(username);
    }
}
