package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.exceptions.UserNotFoundException;
import org.appslapp.AppsLappServer.persistance.users.LabmasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LabmasterService implements EntityService<Labmaster> {
    private final LabmasterRepository labmasterRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public LabmasterService(LabmasterRepository labmasterRepository, PasswordEncoder encoder) {
        this.labmasterRepository = labmasterRepository;
        this.encoder = encoder;
    }

    @Override
    public Labmaster getUserByName(String name) {
        return labmasterRepository.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name));
    }

    public long save(Labmaster master) {
        master.setPassword(encoder.encode(master.getPassword()));
        return labmasterRepository.save(master).getId();
    }

    public long update(Labmaster master) {
        return labmasterRepository.save(master).getId();
    }
}
