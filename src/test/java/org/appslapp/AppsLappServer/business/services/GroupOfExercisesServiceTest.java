package org.appslapp.AppsLappServer.business.services;

import static org.mockito.Mockito.*;


import org.appslapp.AppsLappServer.business.pojo.GroupOfExercises;
import org.appslapp.AppsLappServer.exceptions.GroupOfExercisesNotFoundException;
import org.appslapp.AppsLappServer.persistance.GroupOfExercisesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupOfExercisesServiceTest {

    @Mock
    GroupOfExercisesRepository groupOfExercisesRepository;

    @InjectMocks
    GroupOfExercisesService example;

    @Test
    public void testSave() {
        GroupOfExercises groupOfExercises = new GroupOfExercises();
        groupOfExercises.setId(1L);
        when(groupOfExercisesRepository.save(groupOfExercises)).thenReturn(groupOfExercises);

        long id = example.save(groupOfExercises);

        assertEquals(1L, id);
    }

    @Test
    public void testGetGroupOfExercisesByName_whenFound() {
        GroupOfExercises groupOfExercises = new GroupOfExercises();
        groupOfExercises.setId(1L);
        groupOfExercises.setName("Group1");
        when(groupOfExercisesRepository.findByName("Group1")).thenReturn(Optional.of(groupOfExercises));

        GroupOfExercises returnedGroupOfExercises = example.getGroupOfExercisesByName("Group1");

        assertEquals(groupOfExercises, returnedGroupOfExercises);
    }

    @Test
    public void testGetGroupOfExercisesByName_whenNotFound() {
        when(groupOfExercisesRepository.findByName("Group1")).thenReturn(Optional.empty());

        assertThrows(GroupOfExercisesNotFoundException.class, () -> example.getGroupOfExercisesByName("Group1"));
    }

    @Test
    public void testFindAll() {
        List<GroupOfExercises> groupOfExercises = Arrays.asList(new GroupOfExercises(), new GroupOfExercises());
        when(groupOfExercisesRepository.findAll()).thenReturn(groupOfExercises);

        List<GroupOfExercises> returnedGroupOfExercises = example.findAll();

        assertEquals(groupOfExercises, returnedGroupOfExercises);
    }

}