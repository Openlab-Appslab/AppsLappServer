package org.appslapp.AppsLappServer.business.services;

import org.appslapp.AppsLappServer.business.pojo.GroupOfExercises;
import org.appslapp.AppsLappServer.exceptions.GroupOfExercisesNotFoundException;
import org.appslapp.AppsLappServer.persistance.GroupOfExercisesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupOfExercisesService {

    private final GroupOfExercisesRepository groupOfExercisesRepository;

    @Autowired
    public GroupOfExercisesService(GroupOfExercisesRepository groupOfExercisesRepository) {
        this.groupOfExercisesRepository = groupOfExercisesRepository;
    }

    public Long save(GroupOfExercises groupOfExercises) {
        return groupOfExercisesRepository.save(groupOfExercises).getId();
    }

    public GroupOfExercises getGroupOfExercisesByName(String name) {
        Optional<GroupOfExercises> result = groupOfExercisesRepository.findByName(name);
        if(result.isEmpty()) {
            throw new GroupOfExercisesNotFoundException(name);
        }

        return result.get();
    }

    public List<GroupOfExercises> findAll() {
        return groupOfExercisesRepository.findAllByOrderByDescriptionDesc();
    }
}
