package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.Rank;

import java.util.UUID;

public interface RankService {

    Rank getRankById(UUID id);

    Rank getRankByName(String name);
}
