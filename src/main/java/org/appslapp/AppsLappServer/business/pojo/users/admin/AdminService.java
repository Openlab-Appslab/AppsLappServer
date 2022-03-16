package org.appslapp.AppsLappServer.business.pojo.users.admin;

import org.appslapp.AppsLappServer.persistance.users.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(@Autowired AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Optional<Admin> getUserByName(String username) {
        return adminRepository.findByUsername(username);
    }

}
