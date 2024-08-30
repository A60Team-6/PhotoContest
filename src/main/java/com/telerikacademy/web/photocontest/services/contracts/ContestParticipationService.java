package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.User;

import java.util.List;
import java.util.UUID;

public interface ContestParticipationService {
    List<ContestParticipation> getAll();

    void participateInContest(User user, UUID id);
}
