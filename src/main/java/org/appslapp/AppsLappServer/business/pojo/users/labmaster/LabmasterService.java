package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import org.appslapp.AppsLappServer.persistance.users.LabmasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LabmasterService {
    private final LabmasterRepository labmasterRepository;

    public LabmasterService(@Autowired LabmasterRepository labmasterRepository) {
        this.labmasterRepository = labmasterRepository;
    }

    public Optional<Labmaster> getByUsername(String username) {
        return labmasterRepository.findByUsername(username);
    }

    public long save(Labmaster master) {
        return labmasterRepository.save(master).getId();
    }
}
