package org.appslapp.AppsLappServer.business.pojo.users.admin;

import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.persistance.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService implements EntityService<Admin> {
    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;

    public AdminService(@Autowired AdminRepository adminRepository, @Autowired PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public Optional<Admin> getUserByName(String username) {
        return adminRepository.findByUsername(username);
    }

    public void save(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

}
