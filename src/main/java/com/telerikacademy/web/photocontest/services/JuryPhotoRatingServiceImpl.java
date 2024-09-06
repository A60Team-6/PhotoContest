package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutput;
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
        return juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId).stream()
                .map(rating -> conversionService.convert(rating, JuryPhotoRatingOutput.class))
                .collect(Collectors.toList());
    }
// ToDo integrate map to conver
    @Override
    public JuryPhotoRatingOutput getRatingById(UUID id) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
    }

    // Todo this method is making several things at once, separate logic into smaller methods...

//    @Override
//    public JuryPhotoRatingOutput createRating(JuryPhotoRatingInput input) {
//        Photo photo = validateAndFetchPhoto(input);
//        User user = validateAndFetchUser(input);
//
//        JuryPhotoRating rating = buildRating(input, photo, user);
//
//        return convertRatingToOutput(rating);
//    }
//
//    // Validation and fetch for photo
//    private Photo validateAndFetchPhoto(JuryPhotoRatingInput input) {
//        return Optional.of(photoService.findPhotoEntityById(input.getPhotoId()))
//                .filter(photo -> "Phase 2".equals(photo.getContest().getPhase().getName()))
//                .orElseThrow(() -> new IllegalArgumentException("You can rate a photo only in Phase 2"));
//    }
//
//    // Validation and fetch for user
//    private User validateAndFetchUser(JuryPhotoRatingInput input) {
//        return Optional.of(userService.findUserEntityById(input.getUserId()))
//                .filter(user -> !user.getRole().getName().equals("User"))
//                .orElseThrow(() -> new IllegalArgumentException("You can not rate a photo if you are not Jury or Organizer!"));
//    }
//
//    // Build the JuryPhotoRating
//    private JuryPhotoRating buildRating(JuryPhotoRatingInput input, Photo photo, User user) {
//        return juryPhotoRatingRepository.save(
//                JuryPhotoRating.builder()
//                        .photo(photo)
//                        .jury(user)
//                        .score(input.getScore())
//                        .comment(input.getComment())
//                        .categoryMatch(input.getCategoryMatch())
//                        .reviewDate(LocalDateTime.now())
//                        .isActive(true)
//                        .build()
//        );
//    }
//
//    // Convert to output
//    private JuryPhotoRatingOutput convertRatingToOutput(JuryPhotoRating rating) {
//        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
//    }

    @Override
    public JuryPhotoRatingOutput createRating(JuryPhotoRatingInput input) {
        Photo photo = photoService.findPhotoEntityById(input.getPhotoId());
        Contest contest = photo.getContest();

        if(!contest.getPhase().getName().equals("Phase 2")){
            throw new IllegalArgumentException("You can rate a photo only in Phase 2");
        }

        User user = userService.findUserEntityById(input.getUserId());

        if(user.getRole().getName().equals("User")){
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

        rating = juryPhotoRatingRepository.save(rating);

        return conversionService.convert(rating, JuryPhotoRatingOutput.class);
    }

    // ToDo all of your delete methods can be made in single DB query instead of doing this
    // ToDo EXPENSIVE operation as save insted - > @Modifying
    //@Query("UPDATE JuryPhotoRating r SET r.isActive = false WHERE r.id = :id AND r.isActive = true")
    //void softDeleteById(@Param("id") UUID id); ToDO this should be in your repository
    @Override
    public void softDeleteRating(UUID id) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        rating.setIsActive(false);

        juryPhotoRatingRepository.save(rating);
    }

    // tOdo this logic is very smell... can you think of ways to improve it ? I will give you hints check it..
//    @Override
//    public JuryPhotoRatingOutput updateRating(UUID id, JuryPhotoRatingInput input) {
//        return juryPhotoRatingRepository.findById(id)
//                .map(rating -> updateFields(rating, input))
//                .map(juryPhotoRatingRepository::save)
//                .map(r -> conversionService.convert(r, JuryPhotoRatingOutput.class))
//                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));
//    }
//
//    private JuryPhotoRating updateFields(JuryPhotoRating rating, JuryPhotoRatingInput input) {
//        return Optional.of(rating)
//                .filter(r -> isRatingChanged(r, input)) // Only update if changes are present
//                .map(r -> {
//                    r.setScore(input.getScore());
//                    r.setComment(input.getComment());
//                    r.setCategoryMatch(input.getCategoryMatch());
//                    return r;
//                })
//                .orElse(rating);
//    }
//
//    private boolean isRatingChanged(JuryPhotoRating rating, JuryPhotoRatingInput input) {
//        return !Objects.equals(rating.getScore(), input.getScore()) ||
//                !Objects.equals(rating.getComment(), input.getComment()) ||
//                !Objects.equals(rating.getCategoryMatch(), input.getCategoryMatch());
//    }

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
        User user = userService.findUserEntityById(userId);
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
