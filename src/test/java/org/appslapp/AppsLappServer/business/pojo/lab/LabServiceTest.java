package org.appslapp.AppsLappServer.business.pojo.lab;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.persistance.LabRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

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
        lab.setStudentNames(List.of("haha", "ratata"));
        lab.setName("testik");

        var labmaster = new Labmaster();
        labmaster.setUsername("test");

        // when
        underTest.createLab(lab, labmasterService, "test");

        // then
        var capture = ArgumentCaptor.forClass(Lab.class);
        verify(labRepository).save(capture.capture());

        assertThat(lab).isEqualTo(capture.getValue());
    }
}