package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.models.JuryPhotoRating;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.JuryPhotoRatingInputDto;
import com.telerikacademy.web.photocontest.models.dtos.JuryPhotoRatingOutputDto;
import com.telerikacademy.web.photocontest.repositories.JuryPhotoRatingRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.JuryPhotoRatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuryPhotoRatingServiceImpl implements JuryPhotoRatingService {
    private final JuryPhotoRatingRepository juryPhotoRatingRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final MapperHelper mapperHelper;

    @Override
    public List<JuryPhotoRatingOutputDto> getAllRatingsForPhoto(UUID photoId) {
        return juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId).stream()
                .map(mapperHelper::toJuryPhotoRatingOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public JuryPhotoRatingOutputDto addRating(JuryPhotoRatingInputDto dto) {
        Photo photo = photoRepository.findById(dto.getPhotoId())
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        JuryPhotoRating rating = mapperHelper.toJuryPhotoRating(dto, photo, user);
        JuryPhotoRating savedRating = juryPhotoRatingRepository.save(rating);

        return mapperHelper.toJuryPhotoRatingOutputDto(savedRating);
    }

    @Override
    public void softDeleteRating(UUID id) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        rating.setIsActive(false);
        juryPhotoRatingRepository.save(rating);
    }

    @Override
    public List<JuryPhotoRatingOutputDto> getRatingsByUser(UUID userId) {
        return juryPhotoRatingRepository.findByReviewerIdAndIsActiveTrue(userId).stream()
                .map(mapperHelper::toJuryPhotoRatingOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public JuryPhotoRatingOutputDto updateRating(UUID id, JuryPhotoRatingInputDto dto) {
        JuryPhotoRating rating = juryPhotoRatingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());
        rating.setCategoryMatch(dto.getCategoryMatch());

        JuryPhotoRating updatedRating = juryPhotoRatingRepository.save(rating);

        return mapperHelper.toJuryPhotoRatingOutputDto(updatedRating);
    }


    @Override
    public List<JuryPhotoRatingOutputDto> getRatingsForPhoto(UUID photoId) {
        return juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId).stream()
                .map(mapperHelper::toJuryPhotoRatingOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageScoreForPhoto(UUID photoId) {
        return juryPhotoRatingRepository.findByPhotoIdAndIsActiveTrue(photoId).stream()
                .mapToInt(JuryPhotoRating::getScore)
                .average()
                .orElse(0.0);
    }
}
