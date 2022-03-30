package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.exceptions.LabAlreadyExistsException;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Service
public class LabService {
    private final LabRepository labRepository;

    public LabService(@Autowired LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public long save(Lab lab) {
        return labRepository.save(lab).getId();
    }

    public long createLab(Lab lab, LabmasterService labmasterService, String username) {
        var labmaster = labmasterService.getUserByName(username);
        lab.setLabmaster(labmaster);
        var id = save(lab);
        labmaster.getLabs().add(lab);
        labmasterService.update(labmaster);
        return id;
    }
}
