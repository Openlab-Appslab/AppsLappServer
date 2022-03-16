package org.appslapp.AppsLappServer.persistance.users;

import org.appslapp.AppsLappServer.business.pojo.users.admin.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}
