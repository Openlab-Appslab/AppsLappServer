package org.appslapp.AppsLappServer.business.services;
import static org.mockito.Mockito.*;

import org.appslapp.AppsLappServer.business.pojo.Lab;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.exceptions.LabNotFoundException;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class LabServiceTest {

    @Mock
    LabmasterService labmasterService;

    @Mock
    LabRepository labRepository;

    @InjectMocks
    LabService example;

    @Test
    public void testSave() {
        Lab lab = new Lab();
        lab.setId(1);
        when(labRepository.save(lab)).thenReturn(lab);

        long id = example.save(lab);

        assertEquals(1, id);
    }


    @Test
    public void testCreateLab() {
        Lab lab = new Lab();
        lab.setId(1);
        Labmaster labmaster = new Labmaster();
        labmaster.setId(1L);
        labmaster.setLabs(new ArrayList<>());
        labmaster.setUsername("johndoe");
        when(labmasterService.getUserByName("johndoe")).thenReturn(labmaster);
        when(labmasterService.update(labmaster)).thenReturn(1L);
        when(labRepository.save(lab)).thenReturn(lab);

        long id = example.createLab(lab, "johndoe");

        assertEquals(1, id);
        assertEquals(labmaster, lab.getLabmaster());
        verify(labmasterService).getUserByName("johndoe");
        verify(labmasterService).update(labmaster);
        verify(labRepository).save(lab);
    }

    @Test
    public void testGetLab_whenLabFound() {
        Lab lab = new Lab();
        lab.setId(1);
        when(labRepository.findById(1L)).thenReturn(Optional.of(lab));

        Lab returnedLab = example.getLab(1);

        assertEquals(lab, returnedLab);
    }

    @Test
    public void testGetLab_whenLabNotFound() {
        when(labRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LabNotFoundException.class, () -> example.getLab(1));
    }


}