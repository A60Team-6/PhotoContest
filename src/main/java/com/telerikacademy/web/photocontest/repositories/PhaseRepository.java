package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.models.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, UUID> {
}
