package org.appslapp.AppsLappServer.persistance.users;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LabmasterRepository extends CrudRepository<Labmaster, Long> {
    Optional<Labmaster> findByUsername(String username);
}
