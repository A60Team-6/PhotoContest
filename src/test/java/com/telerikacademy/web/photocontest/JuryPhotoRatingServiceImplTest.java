package com.telerikacademy.web.photocontest;

import com.telerikacademy.web.photocontest.entities.*;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.repositories.JuryPhotoRatingRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.JuryPhotoRatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JuryPhotoRatingServiceImplTest {

    @Mock
    private JuryPhotoRatingRepository juryPhotoRatingRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private JuryPhotoRatingServiceImpl juryPhotoRatingService;

    private JuryPhotoRatingInput ratingInput;
    private JuryPhotoRating rating;
    private JuryPhotoRatingOutput ratingOutput;
    private Photo photo;
    private User user;
    private Contest contest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID photoId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        contest = Contest.builder().phase(Phase.builder().name("Phase 2").build()).build();

        photo = Photo.builder()
                .contest(contest)
                .build();

        user = User.builder()
                .role(Role.builder().name("Jury").build())
                .build();

        ratingInput = JuryPhotoRatingInput.builder()
                .photoId(photoId)
                .userId(userId)
                .score(8)
                .comment("Great photo")
                .categoryMatch(true)
                .build();

        rating = JuryPhotoRating.builder()
                .photo(photo)
                .jury(user)
                .score(8)
                .comment("Great photo")
                .categoryMatch(true)
                .reviewDate(LocalDateTime.now())
                .isActive(true)
                .build();

        ratingOutput = JuryPhotoRatingOutput.builder()
                .photoId(photoId)
                .userId(userId)
                .score(8)
                .comment("Great photo")
                .categoryMatch(true)
                .reviewDate(LocalDateTime.now())
                .build();
    }

    @Test
    void createRating_shouldCreateRating() {
        when(photoRepository.findByIdAndIsActiveTrue(ratingInput.getPhotoId())).thenReturn(photo);
        when(userRepository.findByUserIdAndIsActiveTrue(ratingInput.getUserId())).thenReturn(user);
        when(juryPhotoRatingRepository.save(any(JuryPhotoRating.class))).thenReturn(rating);
        when(conversionService.convert(any(JuryPhotoRating.class), eq(JuryPhotoRatingOutput.class))).thenReturn(ratingOutput);

        JuryPhotoRatingOutput result = juryPhotoRatingService.createRating(ratingInput);

        assertNotNull(result);
        assertEquals(ratingOutput.getScore(), result.getScore());
        assertEquals(ratingOutput.getComment(), result.getComment());
        verify(juryPhotoRatingRepository, times(1)).save(any(JuryPhotoRating.class));
        verify(photoRepository, times(1)).save(any(Photo.class));
    }

    @Test
    void createRating_shouldThrowExceptionWhenPhotoNotFound() {
        when(photoRepository.findByIdAndIsActiveTrue(ratingInput.getPhotoId())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> juryPhotoRatingService.createRating(ratingInput));
    }

    @Test
    void createRating_shouldThrowExceptionWhenUserNotJuryOrOrganizer() {
        user.setRole(Role.builder().name("User").build());
        when(photoRepository.findByIdAndIsActiveTrue(ratingInput.getPhotoId())).thenReturn(photo);
        when(userRepository.findByUserIdAndIsActiveTrue(ratingInput.getUserId())).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> juryPhotoRatingService.createRating(ratingInput));
    }

    @Test
    void getAllRatingsForPhoto_shouldReturnRatings() {
        UUID photoId = UUID.randomUUID();
        when(juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId)).thenReturn(Collections.singletonList(rating));
        when(conversionService.convert(any(JuryPhotoRating.class), eq(JuryPhotoRatingOutput.class))).thenReturn(ratingOutput);

        List<JuryPhotoRatingOutput> result = juryPhotoRatingService.getAllRatingsForPhoto(photoId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ratingOutput.getScore(), result.get(0).getScore());
    }

    @Test
    void softDeleteRating_shouldDeactivateRating() {
        UUID ratingId = UUID.randomUUID();
        when(juryPhotoRatingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        juryPhotoRatingService.softDeleteRating(ratingId);

        assertFalse(rating.getIsActive());
        verify(juryPhotoRatingRepository, times(1)).save(rating);
    }

    @Test
    void updateRating_shouldUpdateRating() {
        UUID ratingId = UUID.randomUUID();
        when(juryPhotoRatingRepository.findById(ratingId)).thenReturn(Optional.of(rating));
        when(juryPhotoRatingRepository.save(any(JuryPhotoRating.class))).thenReturn(rating);
        when(conversionService.convert(any(JuryPhotoRating.class), eq(JuryPhotoRatingOutput.class))).thenReturn(ratingOutput);

        JuryPhotoRatingOutput result = juryPhotoRatingService.updateRating(ratingId, ratingInput);

        assertNotNull(result);
        assertEquals(ratingOutput.getScore(), result.getScore());
        assertEquals(ratingOutput.getComment(), result.getComment());
        verify(juryPhotoRatingRepository, times(1)).save(any(JuryPhotoRating.class));
    }

    @Test
    void getRatingById_shouldReturnRating() {
        UUID ratingId = UUID.randomUUID();
        when(juryPhotoRatingRepository.findById(ratingId)).thenReturn(Optional.of(rating));
        when(conversionService.convert(any(JuryPhotoRating.class), eq(JuryPhotoRatingOutput.class))).thenReturn(ratingOutput);

        JuryPhotoRatingOutput result = juryPhotoRatingService.getRatingById(ratingId);

        assertNotNull(result);
        assertEquals(ratingOutput.getScore(), result.getScore());
        verify(juryPhotoRatingRepository, times(1)).findById(ratingId);
    }

    @Test
    void getRatingById_shouldThrowExceptionWhenNotFound() {
        UUID ratingId = UUID.randomUUID();
        when(juryPhotoRatingRepository.findById(ratingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> juryPhotoRatingService.getRatingById(ratingId));
    }

    @Test
    void getAverageScoreForPhoto_shouldReturnAverageScore() {
        UUID photoId = UUID.randomUUID();
        when(juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId)).thenReturn(Collections.singletonList(rating));

        double result = juryPhotoRatingService.getAverageScoreForPhoto(photoId);

        assertEquals(8.0, result);
    }

    @Test
    void getRatingsByUser_shouldReturnRatings() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserIdAndIsActiveTrue(userId)).thenReturn(user);
        when(juryPhotoRatingRepository.findRatingsByJury(user)).thenReturn(Collections.singletonList(rating));
        when(conversionService.convert(any(JuryPhotoRating.class), eq(JuryPhotoRatingOutput.class))).thenReturn(ratingOutput);

        List<JuryPhotoRatingOutput> result = juryPhotoRatingService.getRatingsByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ratingOutput.getScore(), result.get(0).getScore());
    }
}
