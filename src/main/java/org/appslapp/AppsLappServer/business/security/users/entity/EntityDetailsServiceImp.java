package org.appslapp.AppsLappServer.business.security.users.entity;

import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EntityDetailsServiceImp<K extends Entity, T extends EntityService<K>> {
    private final T entityService;

    public EntityDetailsServiceImp(T entityService) {
        this.entityService = entityService;
    }

    public K getUserByUsername(String username) throws UsernameNotFoundException {
        return entityService.getUserByName(username);
    }
}
