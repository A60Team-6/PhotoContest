package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ContestParticipationRepository extends JpaRepository<ContestParticipation, UUID> {
}
