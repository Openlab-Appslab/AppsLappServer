package org.appslapp.AppsLappServer.business.services;

import org.appslapp.AppsLappServer.business.Dto.LabDto;
import org.appslapp.AppsLappServer.business.pojo.Lab;
import org.appslapp.AppsLappServer.business.pojo.users.entity.Entity;
import org.appslapp.AppsLappServer.exceptions.LabNotFoundException;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LabService {
    private final LabRepository labRepository;
    private final LabmasterService service;

    @Autowired
    public LabService(LabRepository labRepository, LabmasterService service) {
        this.labRepository = labRepository;
        this.service = service;
    }

    public long save(Lab lab) {
        return labRepository.save(lab).getId();
    }

    public long createLab(Lab lab, String username) {
        var labmaster = service.getUserByName(username);
        labmaster.getLabs().add(lab);
        service.update(labmaster);
        lab.setLabmaster(labmaster);
        return save(lab);
    }

    public Lab getLab(long id) {
        return labRepository.findById(id).orElseThrow();
    }

    public LabDto getLabDto(long labId) {
        Optional<Lab> lab = labRepository.findById(labId);
        if (lab.isEmpty()) {
            throw new LabNotFoundException(labId);
        }

        var l = lab.get();
        var ret = new LabDto();
        ret.setName(l.getName());
        ret.setGroupOfExercises(l.getGroupOfExercises());
        ret.setId(l.getId());
        ret.setLabmaster(l.getLabmaster());
        ret.setStudentNames(l.getStudentNames().stream().map(Entity::getUsername).collect(Collectors.toList()));
        return ret;
    }
}
