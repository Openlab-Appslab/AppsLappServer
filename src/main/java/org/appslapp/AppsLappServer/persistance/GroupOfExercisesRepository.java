package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupOfExercisesRepository extends CrudRepository<GroupOfExercises, Long> {
}
