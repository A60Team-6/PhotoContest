package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.JuryPhotoRating;
import com.telerikacademy.web.photocontest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JuryPhotoRatingRepository extends JpaRepository<JuryPhotoRating, UUID> {

    List<JuryPhotoRating> findByPhotoIdAndIsActiveTrue(UUID photoId);

    List<JuryPhotoRating> findRatingsByJury(User user);
}