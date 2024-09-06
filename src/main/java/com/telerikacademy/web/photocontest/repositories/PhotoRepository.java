package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    List<Photo> findAllByIsActiveTrue();

    Photo findByIdAndIsActiveTrue(UUID id);

    Photo findByTitleAndIsActiveTrue(String title);

    List<Photo> findByTitleContainingIgnoreCase(String title);

    boolean existsByTitleAndIsActiveTrue(String title);

    boolean existsByHash(String hash);

    List<Photo> findAllByContestAndIsActiveTrue(Contest contest);
}

