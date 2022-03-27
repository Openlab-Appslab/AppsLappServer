package org.appslapp.AppsLappServer.business.pojo.users.entity;

public interface EntityService<T extends Entity> {
    T getUserByName(String name);
}
