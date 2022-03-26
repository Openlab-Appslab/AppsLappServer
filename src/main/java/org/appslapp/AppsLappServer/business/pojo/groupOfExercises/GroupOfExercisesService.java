package org.appslapp.AppsLappServer.business.pojo.groupOfExercises;

import org.appslapp.AppsLappServer.persistance.GroupOfExercisesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupOfExercisesService {

    private final GroupOfExercisesRepository groupOfExercisesRepository;

    public GroupOfExercisesService(@Autowired GroupOfExercisesRepository groupOfExercisesRepository) {
        this.groupOfExercisesRepository = groupOfExercisesRepository;
    }


    public Long save(GroupOfExercises groupOfExercises) {
        return groupOfExercisesRepository.save(groupOfExercises).getId();
    }
}
