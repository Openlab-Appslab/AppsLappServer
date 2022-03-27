package org.appslapp.AppsLappServer.business.pojo.users.admin;

import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.exceptions.UserNotFoundException;
import org.appslapp.AppsLappServer.persistance.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements EntityService<Admin> {
    private final AdminRepository adminRepository;
    private final PasswordEncoder encoder;

    public AdminService(@Autowired AdminRepository adminRepository, @Autowired PasswordEncoder encoder) {
        this.adminRepository = adminRepository;
        this.encoder = encoder;
    }

    public Admin getUserByName(String username) {
        return adminRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public void save(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

}
