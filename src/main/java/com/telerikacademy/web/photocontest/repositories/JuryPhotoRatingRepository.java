package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JuryPhotoRatingRepository extends JpaRepository<JuryPhotoRating, UUID> {

    List<JuryPhotoRating> findByPhotoId(UUID photoId);

//    List<JuryPhotoRating> findByUserId(UUID userId);

//    Optional<JuryPhotoRating> findByPhotoIdAndUserId(UUID photoId, UUID userId);

//    List<JuryPhotoRating> findAllByPhotoIdAndUserId(UUID photoId, UUID userId);
}