package org.appslapp.AppsLappServer.business.services;

import org.appslapp.AppsLappServer.business.pojo.Exercise;
import org.appslapp.AppsLappServer.exceptions.ExerciseNotFoundException;
import org.appslapp.AppsLappServer.persistance.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final GroupOfExercisesService service;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, GroupOfExercisesService service) {
        this.exerciseRepository = exerciseRepository;
        this.service = service;
    }

    public long save(Exercise exercise, boolean isUpdate) {
        if (isUpdate)
            service.save(exercise.getGroupOfExercises());
        return exerciseRepository.save(exercise).getId();
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public Exercise getExercise(long exerciseId) {
        return exerciseRepository.findById(exerciseId).orElseThrow(ExerciseNotFoundException::new);
    }

    public Exercise getExerciseByName(String exerciseName) {
        return exerciseRepository.findByName(exerciseName).orElseThrow(ExerciseNotFoundException::new);
    }

    public void deleteExercise(String exerciseName) {
        exerciseRepository.findByName(exerciseName).ifPresent(exerciseRepository::delete);
    }
}
