package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.lab.Lab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends CrudRepository<Lab, Long> {
    List<Lab> findAllByLabMasterId(long id);
}
