package org.appslapp.AppsLappServer.business.security.users.labmaster;

import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class LabmasterDetailsImp extends EntityDetailsImp<Labmaster> {
    public LabmasterDetailsImp(Labmaster labmaster) {
        super(labmaster);
    }

    public Lab getLab() {
        return user.getLab();
    }
}
