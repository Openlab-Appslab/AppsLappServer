package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.persistance.users.LabmasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LabmasterService implements EntityService<Labmaster> {
    private final LabmasterRepository labmasterRepository;
    private final PasswordEncoder encoder;

    public LabmasterService(@Autowired LabmasterRepository labmasterRepository, @Autowired PasswordEncoder encoder) {
        this.labmasterRepository = labmasterRepository;
        this.encoder = encoder;
    }

    @Override
    public Optional<Labmaster> getUserByName(String name) {
        return labmasterRepository.findByUsername(name);
    }

    public long save(Labmaster master) {
        master.setPassword(encoder.encode(master.getPassword()));
        return labmasterRepository.save(master).getId();
    }
}
