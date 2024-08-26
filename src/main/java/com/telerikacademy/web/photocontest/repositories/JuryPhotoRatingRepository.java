package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.models.JuryPhotoRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JuryPhotoRatingRepository extends JpaRepository<JuryPhotoRating, UUID> {

    List<JuryPhotoRating> findByPhotoIdAndIsActiveTrue(UUID photoId);

    List<JuryPhotoRating> findByReviewerIdAndIsActiveTrue(UUID userId);



}