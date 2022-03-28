package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public long createLab(Map<String, String> rawLab, LabmasterService labmasterService, String username) {
        Lab lab = new Lab();
        lab.setName(rawLab.get("name"));
        lab.setStudentNames(List.of(rawLab.get(("studentNames")).split(",,,")));
        var labmaster = labmasterService.getUserByName(username);
        lab.setLabmaster(labmaster);
        var id = save(lab);
        labmaster.setLab(lab);
        labmasterService.save(labmaster);
        return id;
    }
}
