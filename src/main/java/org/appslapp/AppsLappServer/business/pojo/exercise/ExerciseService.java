package org.appslapp.AppsLappServer.business.pojo.exercise;

import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercisesService;
import org.appslapp.AppsLappServer.persistance.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public long save(Exercise exercise, GroupOfExercisesService service) {
        service.save(exercise.getGroupOfExercises());
        Long id = exerciseRepository.save(exercise).getId();
        return id;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }
}
