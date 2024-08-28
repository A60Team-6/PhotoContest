package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.repositories.JuryPhotoRatingRepository;
import com.telerikacademy.web.photocontest.services.contracts.JuryPhotoRatingService;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
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
    private final PhotoService photoService;
    private final UserService userService;
    private final ConversionService conversionService;

    @Override
    public List<JuryPhotoRatingOutput> getAllRatingsForPhoto(UUID photoId) {
        return juryPhotoRatingRepository.findByPhotoId(photoId).stream()
                .map(rating -> conversionService.convert(rating, JuryPhotoRatingOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public JuryPhotoRatingOutput getRatingById(UUID id) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
    }

    @Override
    public JuryPhotoRatingOutput createRating(JuryPhotoRatingInput input) {
        Photo photo = photoService.findPhotoEntityById(input.getPhotoId());

        User user = userService.findUserEntityById(input.getUserId());

        JuryPhotoRating rating = JuryPhotoRating.builder()
                .photo(photo)
                .jury(user)
                .score(input.getScore())
                .comment(input.getComment())
                .categoryMatch(input.getCategoryMatch())
                .reviewDate(LocalDateTime.now())
                .isActive(true)
                .build();

        rating = juryPhotoRatingRepository.save(rating);

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

//    @Override
//    public List<JuryPhotoRatingOutput> getRatingsByUser(UUID userId) {
//        return juryPhotoRatingRepository.findByUserId(userId).stream()
//                .map(rating -> conversionService.convert(rating, JuryPhotoRatingOutput.class))
//                .collect(Collectors.toList());
//    }



//    @Override
//    public List<JuryPhotoRatingOutput> getRatingsForPhoto(UUID photoId, UUID userId) {
//        List<JuryPhotoRating> ratings = juryPhotoRatingRepository.findAllByPhotoIdAndUserId(photoId, userId);
//
//        if (ratings.isEmpty()) {
//            throw new EntityNotFoundException("No ratings found for this photo and user.");
//        }
//
//        return ratings.stream()
//                .map(rating -> conversionService.convert(rating, JuryPhotoRatingOutput.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public double getAverageScoreForPhoto(UUID photoId) {
            List<JuryPhotoRating> ratings = juryPhotoRatingRepository.findByPhotoId(photoId);
            return ratings.stream()
                    .mapToInt(JuryPhotoRating::getScore)
                    .average()
                    .orElse(0.0);
    }
}
