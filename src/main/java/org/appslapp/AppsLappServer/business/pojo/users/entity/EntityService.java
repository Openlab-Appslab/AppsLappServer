package org.appslapp.AppsLappServer.business.pojo.users.entity;

import java.util.Optional;

public interface EntityService<T extends Entity> {
    Optional<T> getUserByName(String name);
}
