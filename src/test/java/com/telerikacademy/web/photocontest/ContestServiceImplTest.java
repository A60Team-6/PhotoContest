package com.telerikacademy.web.photocontest;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.Role;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.services.ContestServiceImpl;
import com.telerikacademy.web.photocontest.services.contracts.PhaseService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ContestServiceImplTest {

    @Mock
    private ContestRepository contestRepository;

    @Mock
    private ConversionService conversionService;

    @Mock
    private PhaseService phaseService;

    @InjectMocks
    private ContestServiceImpl contestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllActive_ShouldReturnAllActiveContests() {
        // Arrange
        Contest contest1 = new Contest();
        Contest contest2 = new Contest();
        when(contestRepository.findAllByIsActiveTrue()).thenReturn(List.of(contest1, contest2));
        when(conversionService.convert(any(Contest.class), eq(ContestOutput.class))).thenReturn(new ContestOutput());

        // Act
        List<ContestOutput> result = contestService.getAllActive();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(contestRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void testGetAllContests_ShouldReturnAllContests() {
        // Arrange
        Contest contest1 = new Contest();
        Contest contest2 = new Contest();
        when(contestRepository.findAll()).thenReturn(List.of(contest1, contest2));
        when(conversionService.convert(any(Contest.class), eq(ContestOutput.class))).thenReturn(new ContestOutput());

        // Act
        List<ContestOutput> result = contestService.getAllContests();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(contestRepository, times(1)).findAll();
    }

    @Test
    void testFindContestById_ShouldReturnContestOutput_WhenContestExists() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        Contest contest = new Contest();
        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(contest);
        when(conversionService.convert(any(Contest.class), eq(ContestOutput.class))).thenReturn(new ContestOutput());

        // Act
        ContestOutput result = contestService.findContestById(contestId);

        // Assert
        assertNotNull(result);
        verify(contestRepository, times(1)).findByContestIdAndIsActiveTrue(contestId);
    }

    @Test
    void testFindContestById_ShouldThrowEntityNotFoundException_WhenContestDoesNotExist() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> contestService.findContestById(contestId));
    }

    @Test
    void testFindContestEntityById_ShouldReturnContest_WhenContestExistsAndIsActive() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        Contest contest = new Contest();
        contest.setContestId(contestId);
        contest.setIsActive(true);

        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(contest);

        // Act
        Contest result = contestService.findContestEntityById(contestId);

        // Assert
        assertNotNull(result);
        assertEquals(contestId, result.getContestId());
        verify(contestRepository, times(1)).findByContestIdAndIsActiveTrue(contestId);
    }

    @Test
    void testFindContestEntityById_ShouldThrowEntityNotFoundException_WhenContestDoesNotExistOrIsInactive() {
        // Arrange
        UUID contestId = UUID.randomUUID();

        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> contestService.findContestEntityById(contestId));
        verify(contestRepository, times(1)).findByContestIdAndIsActiveTrue(contestId);
    }

    @Test
    void testFindContestByTitle_ShouldReturnContestOutput_WhenTitleExists() {
        // Arrange
        String title = "Contest Title";
        Contest contest = new Contest();
        when(contestRepository.findByTitleAndIsActiveTrue(title)).thenReturn(contest);
        when(conversionService.convert(any(Contest.class), eq(ContestOutput.class))).thenReturn(new ContestOutput());

        // Act
        ContestOutput result = contestService.findContestByTitle(title);

        // Assert
        assertNotNull(result);
        verify(contestRepository, times(1)).findByTitleAndIsActiveTrue(title);
    }

    @Test
    void testCreateContest_ShouldCreateAndReturnContestOutputId() {
        // Arrange
        ContestInput contestInput = new ContestInput("Title", "Category", "http://url.com", 24);
        Role role = new Role();
        role.setName("Organizer");
        User user = new User();
        user.setRole(role);

        Phase phase = new Phase();
        when(phaseService.getPhaseByName("Phase 1")).thenReturn(phase);
        when(contestRepository.save(any(Contest.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(conversionService.convert(any(Contest.class), eq(ContestOutputId.class))).thenReturn(new ContestOutputId(UUID.randomUUID().toString()));

        // Act
        ContestOutputId result = contestService.createContest(contestInput, user);

        // Assert
        assertNotNull(result);
        verify(contestRepository, times(1)).save(any(Contest.class));
    }

    @Test
    void testCreateContest_ShouldThrowDuplicateEntityException_WhenTitleExists() {
        // Arrange
        ContestInput contestInput = new ContestInput("Title", "Category", "http://url.com", 24);
        Role role = new Role();
        role.setName("Organizer");
        User user = new User();
        user.setRole(role);

        when(contestRepository.findByTitleAndIsActiveTrue(anyString())).thenReturn(new Contest());

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> contestService.createContest(contestInput, user));
    }

    @Test
    void testDeactivateContest_ShouldDeactivateContest_WhenContestExists() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        Role role = new Role();
        role.setName("Organizer");
        User user = new User();
        user.setRole(role);

        Contest contest = new Contest();
        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(contest);

        // Act
        contestService.deactivateContest(contestId, user);

        // Assert
        assertFalse(contest.getIsActive());
        verify(contestRepository, times(1)).save(contest);
    }

    @Test
    void testDeactivateContest_ShouldThrowEntityNotFoundException_WhenContestDoesNotExist() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        Role role = new Role();
        role.setName("Organizer");
        User user = new User();
        user.setRole(role);

        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> contestService.deactivateContest(contestId, user));
    }

    @Test
    void testUpdateContestPhases_ShouldUpdatePhases() {
        // Arrange
        Phase phase1 = new Phase();
        phase1.setName("Phase 1");

        Phase phase2 = new Phase();
        phase2.setName("Phase 2");

        Contest contest = new Contest();
        contest.setPhase(phase1);
        contest.setChangePhaseTime(LocalDateTime.now().minusMinutes(5));

        when(contestRepository.findAllByIsActiveTrue()).thenReturn(List.of(contest));
        when(phaseService.getPhaseByName("Phase 2")).thenReturn(phase2);

        // Act
        contestService.updateContestPhases();

        // Assert
        verify(contestRepository, times(1)).save(contest);
        assertEquals("Phase 2", contest.getPhase().getName());
    }
}
