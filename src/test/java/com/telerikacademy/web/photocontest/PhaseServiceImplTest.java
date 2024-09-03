package com.telerikacademy.web.photocontest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.repositories.PhaseRepository;
import com.telerikacademy.web.photocontest.services.PhaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhaseServiceImplTest {

    @Mock
    private PhaseRepository phaseRepository;

    @InjectMocks
    private PhaseServiceImpl phaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPhaseById_ShouldReturnNull_WhenPhaseDoesNotExist() {
        // Arrange
        UUID phaseId = UUID.randomUUID();
        when(phaseRepository.getReferenceById(phaseId)).thenReturn(null);

        // Act
        Phase result = phaseService.getPhaseById(phaseId);

        // Assert
        assertNull(result);
        verify(phaseRepository, times(1)).getReferenceById(phaseId);
    }

    @Test
    void testGetPhaseByName_ShouldReturnPhase_WhenPhaseExists() {
        // Arrange
        String phaseName = "Phase 1";
        Phase phase = new Phase();
        phase.setName(phaseName);

        when(phaseRepository.findByName(phaseName)).thenReturn(phase);

        // Act
        Phase result = phaseService.getPhaseByName(phaseName);

        // Assert
        assertNotNull(result);
        assertEquals(phaseName, result.getName());
        verify(phaseRepository, times(1)).findByName(phaseName);
    }

    @Test
    void testGetPhaseByName_ShouldReturnNull_WhenPhaseDoesNotExist() {
        // Arrange
        String phaseName = "NonExistentPhase";
        when(phaseRepository.findByName(phaseName)).thenReturn(null);

        // Act
        Phase result = phaseService.getPhaseByName(phaseName);

        // Assert
        assertNull(result);
        verify(phaseRepository, times(1)).findByName(phaseName);
    }
}
