package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.repositories.JuryPhotoRatingRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.contracts.JuryPhotoRatingService;
import com.telerikacademy.web.photocontest.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuryPhotoRatingServiceImpl implements JuryPhotoRatingService {
    private final JuryPhotoRatingRepository juryPhotoRatingRepository;
    //private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    //private final UserService userService;
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Override
    public List<JuryPhotoRatingOutput> getAllRatingsForPhoto(UUID photoId) {
        return juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId).stream()
                .map(rating -> conversionService.convert(rating, JuryPhotoRatingOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JuryPhotoRating> getAllRatingsEntityForPhoto(UUID photoId) {
        return juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId);
    }

    @Override
    public JuryPhotoRatingOutput getRatingById(UUID id) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
    }

    @Override
    public JuryPhotoRatingOutput createRating(JuryPhotoRatingInput input) {
        //Photo photo = photoService.findPhotoEntityById(input.getPhotoId());
        Photo photo = photoRepository.findByIdAndIsActiveTrue(input.getPhotoId());

        if (photo == null) {
            throw new EntityNotFoundException("Photo not found or inactive.");
        }

        Contest contest = photo.getContest();

        if(!"Phase 2".equals(contest.getPhase().getName())){
            throw new IllegalArgumentException("You can rate a photo only in Phase 2");
        }

        //User user = userService.findUserEntityById(input.getUserId());
        User user = userRepository.findByUserIdAndIsActiveTrue(input.getUserId());

        if("User".equals(user.getRole().getName())){
            throw new IllegalArgumentException("You can not rate a photo if you are not Jury or Organizer!");
        }

        JuryPhotoRating rating = JuryPhotoRating.builder()
                .photo(photo)
                .jury(user)
                .score(input.getScore())
                .comment(input.getComment())
                .categoryMatch(input.getCategoryMatch())
                .reviewDate(LocalDateTime.now())
                .isActive(true)
                .build();

        //photo.setTotal_score(getAverageScoreForPhoto(photo.getId()));
        rating = juryPhotoRatingRepository.save(rating);

        double averageScore = getAverageScoreForPhoto(photo.getId());
        photo.setTotal_score(averageScore);
        photoRepository.save(photo);

        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
    }

    @Override
    public void softDeleteRating(UUID id) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        rating.setIsActive(false);

        juryPhotoRatingRepository.save(rating);
    }

    @Override
    public JuryPhotoRatingOutput updateRating(UUID id, JuryPhotoRatingInput input) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        rating.setScore(input.getScore());
        rating.setComment(input.getComment());
        rating.setCategoryMatch(input.getCategoryMatch());

        rating = juryPhotoRatingRepository.save(rating);

        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
    }

    @Override
    public List<JuryPhotoRatingOutput> getRatingsByUser(UUID userId) {
        //User user = userService.findUserEntityById(userId);
        User user = userRepository.findByUserIdAndIsActiveTrue(userId);
        return juryPhotoRatingRepository.findRatingsByJury(user).stream()
                .map(rating -> conversionService.convert(rating, JuryPhotoRatingOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageScoreForPhoto(UUID photoId) {
            List<JuryPhotoRating> ratings = juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId);
            return ratings.stream()
                    .mapToInt(JuryPhotoRating::getScore)
                    .average()
                    .orElse(0.0);
    }
}
