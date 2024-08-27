package com.telerikacademy.web.photocontest.repositories;

import com.telerikacademy.web.photocontest.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RankRepository extends JpaRepository<Rank, UUID> {

    Rank findByName(String name);
}
