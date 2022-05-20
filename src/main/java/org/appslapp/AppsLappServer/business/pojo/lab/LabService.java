package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.exceptions.LabNotFoundException;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Lab getLab(long labId) {
        Optional<Lab> lab = labRepository.findById(labId);
        if (lab.isEmpty()) {
            throw new LabNotFoundException(labId);
        }
        return lab.get();
    }
}
