package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    Photo findByPhotoUrl(String url);

    List<Photo> findAllByIsActiveTrue();

    Photo findByTitleAndIsActiveTrue(String title);

    Photo getById(UUID id);

    List<Photo> findByTitleContainingIgnoreCase(String title);

}
