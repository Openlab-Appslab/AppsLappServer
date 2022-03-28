package org.appslapp.AppsLappServer.persistance.users;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabmasterRepository extends EntityRepository<Labmaster> {
}
