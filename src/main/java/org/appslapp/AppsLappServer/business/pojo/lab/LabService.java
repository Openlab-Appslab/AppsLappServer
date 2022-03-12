package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabService {
    private final LabRepository labRepository;

    public LabService(@Autowired LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public long save(Lab lab) {
        return -1;
    }

    public List<Lab> findAllByLabmasterId(long id) {
        return labRepository.findAllByLabMasterId(id);
    }
}
