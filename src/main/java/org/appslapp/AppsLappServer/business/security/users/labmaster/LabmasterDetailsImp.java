package org.appslapp.AppsLappServer.business.security.users.labmaster;

import org.appslapp.AppsLappServer.business.pojo.Lab;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsImp;

import java.util.List;

public class LabmasterDetailsImp extends EntityDetailsImp<Labmaster> {
    public LabmasterDetailsImp(Labmaster labmaster) {
        super(labmaster);
    }

    public List<Lab> getLabs() {
        return user.getLabs();
    }
}
