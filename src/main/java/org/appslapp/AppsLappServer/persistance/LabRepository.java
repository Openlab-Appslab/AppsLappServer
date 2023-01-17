package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.Lab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends CrudRepository<Lab, Long> {
}
