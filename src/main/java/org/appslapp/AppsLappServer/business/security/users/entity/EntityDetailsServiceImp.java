package org.appslapp.AppsLappServer.business.security.users.entity;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EntityDetailsServiceImp<K extends Entity, T extends EntityService<K>> implements UserDetailsService {
    private final T entityService;

    public EntityDetailsServiceImp(T entityService) {
        this.entityService = entityService;
    }

    @Override
    public EntityDetailsImp<K> loadUserByUsername(String username) throws UsernameNotFoundException {
        return new EntityDetailsImp<>(entityService.getUserByName(username));
    }
}
