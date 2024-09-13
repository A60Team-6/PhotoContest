package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContestRepository extends JpaRepository<Contest, UUID> {


    List<Contest> findAllByIsActiveTrue();

    List<Contest> findAllByIsActiveFalse();

    Contest findByContestIdAndIsActiveTrue(UUID id);

    Contest findByTitleAndIsActiveTrue(String title);

    @Query("SELECT c FROM Contest c " +
            "WHERE (:title IS NULL OR c.title like :title) " +
            "AND (:category IS NULL OR c.category like :category) " +
            "AND (:phase IS NULL OR c.phase.name like :phase) ")
    Page<Contest> findContestsByMultipleFields(
            @Param("title") String title,
            @Param("category") String category,
            @Param("phase") String phase,
            Pageable pageable);

}
