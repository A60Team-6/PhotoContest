package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.Rank;

import java.util.UUID;

public interface RankService {

    Rank getRankById(UUID id);
}
