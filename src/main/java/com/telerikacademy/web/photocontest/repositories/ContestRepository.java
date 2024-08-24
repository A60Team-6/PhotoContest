package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContestRepository extends JpaRepository<Contest, UUID> {


    List<Contest> findAllByIsActiveTrue();

    Contest findByContestIdAndIsActiveTrue(UUID id);

    Contest findByTitleAndIsActiveTrue(String title);

    Contest findByTitle(String title);
}
