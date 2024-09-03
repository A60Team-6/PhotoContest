package com.telerikacademy.web.photocontest;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.helpers.UnauthorizedOperationException;
import com.telerikacademy.web.photocontest.repositories.ContestParticipationRepository;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.services.ContestParticipationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContestParticipationServiceImplTest {

    @Mock
    private ContestParticipationRepository contestParticipationRepository;

    @Mock
    private ContestRepository contestRepository;

    @InjectMocks
    private ContestParticipationServiceImpl contestParticipationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll_ShouldReturnListOfActiveContestParticipation() {
        // Arrange
        ContestParticipation participation = new ContestParticipation();
        when(contestParticipationRepository.findAllByIsActiveTrue()).thenReturn(List.of(participation));

        // Act
        List<ContestParticipation> result = contestParticipationService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contestParticipationRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void testParticipateInContest_ShouldSaveParticipation_WhenContestIsActiveAndInPhase1() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        User user = new User();
        Phase phase = new Phase();
        phase.setName("Phase 1");

        Contest contest = new Contest();
        contest.setContestId(contestId);
        contest.setPhase(phase);

        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(contest);

        // Act
        contestParticipationService.participateInContest(user, contestId);

        // Assert
        verify(contestParticipationRepository, times(1)).save(any(ContestParticipation.class));
    }

    @Test
    void testParticipateInContest_ShouldThrowEntityNotFoundException_WhenContestDoesNotExist() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        User user = new User();

        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(null);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> contestParticipationService.participateInContest(user, contestId));
        verify(contestParticipationRepository, never()).save(any(ContestParticipation.class));
    }

    @Test
    void testParticipateInContest_ShouldThrowUnauthorizedOperationException_WhenContestIsNotInPhase1() {
        // Arrange
        UUID contestId = UUID.randomUUID();
        User user = new User();
        Phase phase = new Phase();
        phase.setName("Phase 2");

        Contest contest = new Contest();
        contest.setContestId(contestId);
        contest.setPhase(phase);

        when(contestRepository.findByContestIdAndIsActiveTrue(contestId)).thenReturn(contest);

        // Act & Assert
        assertThrows(UnauthorizedOperationException.class, () -> contestParticipationService.participateInContest(user, contestId));
        verify(contestParticipationRepository, never()).save(any(ContestParticipation.class));
    }
}
