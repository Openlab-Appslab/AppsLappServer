package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.GroupOfExercises;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupOfExercisesRepository extends CrudRepository<GroupOfExercises, Long> {
    Optional<GroupOfExercises> findByName(String name);
    List<GroupOfExercises> findAllByOrderByDeadlineDesc();
}
