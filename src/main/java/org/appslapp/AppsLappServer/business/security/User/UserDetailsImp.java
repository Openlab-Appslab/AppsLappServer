package org.appslapp.AppsLappServer.business.security.User;

import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.business.security.Entity.EntityDetailsImp;

public class UserDetailsImp extends EntityDetailsImp<User> {
    private final long id;

    public UserDetailsImp(User user) {
        super(user);
        id = user.getId();
    }

    public long getId() {
        return id;
    }
}
