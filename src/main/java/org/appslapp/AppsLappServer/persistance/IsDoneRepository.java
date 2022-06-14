package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.IsDoneExercise.IsDoneExercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IsDoneRepository extends CrudRepository<IsDoneExercise, Long> {
}
