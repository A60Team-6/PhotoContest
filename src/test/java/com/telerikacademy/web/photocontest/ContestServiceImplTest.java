package com.telerikacademy.web.photocontest;

import com.telerikacademy.web.photocontest.entities.*;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.ContestServiceImpl;
import com.telerikacademy.web.photocontest.services.contracts.PhaseService;
import com.telerikacademy.web.photocontest.services.contracts.RankService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private RankService rankService;

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

    @Test
    void decideTop3PlacesAndSetPointsToUsers_shouldAssignPointsCorrectly() {
        // Arrange
        Contest contest = mock(Contest.class);
        Photo photo1 = mock(Photo.class);
        Photo photo2 = mock(Photo.class);
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(photo1.getTotal_score()).thenReturn(90.0);
        when(photo2.getTotal_score()).thenReturn(80.0);
        when(photo1.getUser()).thenReturn(user1);
        when(photo2.getUser()).thenReturn(user2);
        when(photoRepository.findAllByContestAndIsActiveTrue(any())).thenReturn(Arrays.asList(photo1, photo2));

        // Act
        contestService.decideTop3PlacesAndSetPointsToUsers(contest);

        // Assert
        verify(userRepository, times(1)).save(user1);
        verify(userRepository, times(1)).save(user2);
    }

    @Test
    void findMaxScore_shouldReturnHighestScore() {
        // Arrange
        Photo photo1 = mock(Photo.class);
        Photo photo2 = mock(Photo.class);
        when(photo1.getTotal_score()).thenReturn(90.0);
        when(photo2.getTotal_score()).thenReturn(85.0);

        List<Photo> photos = Arrays.asList(photo1, photo2);

        // Act
        double maxScore = contestService.findMaxScore(photos);

        // Assert
        assertEquals(90.0, maxScore);
    }

    @Test
    void findOtherScore_shouldReturnSecondHighestScore() {
        // Arrange
        Photo photo1 = mock(Photo.class);
        Photo photo2 = mock(Photo.class);
        when(photo1.getTotal_score()).thenReturn(90.0);
        when(photo2.getTotal_score()).thenReturn(85.0);

        List<Photo> photos = Arrays.asList(photo1, photo2);

        // Act
        double secondHighestScore = contestService.findOtherScore(photos, 90.0);

        // Assert
        assertEquals(85.0, secondHighestScore);
    }

    @Test
    void filterPhotosByScore_shouldReturnPhotosWithSpecificScore() {
        // Arrange
        Photo photo1 = mock(Photo.class);
        Photo photo2 = mock(Photo.class);
        when(photo1.getTotal_score()).thenReturn(90.0);
        when(photo2.getTotal_score()).thenReturn(90.0);

        List<Photo> photos = Arrays.asList(photo1, photo2);

        // Act
        List<Photo> filteredPhotos = contestService.filterPhotosByScore(photos, 90.0);

        // Assert
        assertEquals(2, filteredPhotos.size());
        assertTrue(filteredPhotos.contains(photo1));
        assertTrue(filteredPhotos.contains(photo2));
    }

    @Test
    void assignPointsToPhotos_shouldAssignPointsBasedOnPhotoCount() {
        // Arrange
        User user1 = mock(User.class);
        Photo photo1 = mock(Photo.class);
        when(photo1.getUser()).thenReturn(user1);

        List<Photo> photos = Arrays.asList(photo1);

        // Act
        contestService.assignPointsToPhotos(photos, 50, 40);

        // Assert
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void addPointsToUser_shouldAddPointsCorrectly() {
        // Arrange
        User user = mock(User.class);
        when(user.getPoints()).thenReturn(100);

        // Act
        contestService.addPointsToUser(user, 50);

        // Assert
        verify(user).setPoints(150);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changeRanking_shouldUpdateUserRankBasedOnPoints() {
        // Arrange
        User user = mock(User.class);
        when(user.getPoints()).thenReturn(1200);

        Rank rank = mock(Rank.class);
        when(rankService.getRankByName("WiseAndBenevolentPhotoDictator")).thenReturn(rank);

        // Act
        contestService.changeRanking(user);

        // Assert
        verify(user).setRank(rank);
        verify(userRepository, times(1)).save(user);
    }


}
