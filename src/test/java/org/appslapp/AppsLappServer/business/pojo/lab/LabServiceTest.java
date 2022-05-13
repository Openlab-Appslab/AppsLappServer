package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabServiceTest {

    @Mock LabRepository labRepository;
    @Mock LabmasterService labmasterService;
    LabService underTest;

    @BeforeEach
    void setUp() {
        underTest = new LabService(labRepository);
    }

    @Test
    void createLab() {
        // given
        var lab = new Lab();
        lab.setId(123);
        lab.setStudentNames(List.of("haha", "ratata"));
        lab.setName("testik");
        var labmaster = new Labmaster();
        labmaster.setLabs(new ArrayList<>());
        labmaster.setUsername("test");
        lab.setLabmaster(labmaster);
        when(labRepository.save(any(Lab.class))).thenReturn(lab);
        when(labmasterService.getUserByName(anyString())).thenReturn(labmaster);
        when(labmasterService.update(any(Labmaster.class))).thenReturn(1L);

        // when
        long id = underTest.createLab(lab, labmasterService, "test");

        // then
        assertThat(id).isEqualTo(123);
    }
}