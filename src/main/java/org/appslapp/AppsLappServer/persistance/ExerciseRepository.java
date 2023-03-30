package org.appslapp.AppsLappServer.persistance;

import org.appslapp.AppsLappServer.business.pojo.Exercise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
    Optional<Exercise> findByName(String exerciseName);

    List<Exercise> findAll();

    void deleteByName(String exerciseName);
}
