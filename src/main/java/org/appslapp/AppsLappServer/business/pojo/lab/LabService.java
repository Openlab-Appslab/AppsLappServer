package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabService {
    private final LabRepository labRepository;

    @Autowired
    public LabService(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public long save(Lab lab) {
        return labRepository.save(lab).getId();
    }

    public long createLab(Lab lab, LabmasterService labmasterService, String username) {
        var labmaster = labmasterService.getUserByName(username);
        labmaster.getLabs().add(lab);
        labmasterService.update(labmaster);
        lab.setLabmaster(labmaster);
        return save(lab);
    }
}
