package org.appslapp.AppsLappServer.business.pojo.users.labmaster;

import org.appslapp.AppsLappServer.persistance.users.LabmasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LabmasterService {
    private final LabmasterRepository labmasterRepository;
    private final PasswordEncoder encoder;

    public LabmasterService(@Autowired LabmasterRepository labmasterRepository, @Autowired PasswordEncoder encoder) {
        this.labmasterRepository = labmasterRepository;
        this.encoder = encoder;
    }

    public Optional<Labmaster> getByUsername(String username) {
        return labmasterRepository.findByUsername(username);
    }

    public long save(Labmaster master) {
        master.setPassword(encoder.encode(master.getPassword()));
        return labmasterRepository.save(master).getId();
    }
}
