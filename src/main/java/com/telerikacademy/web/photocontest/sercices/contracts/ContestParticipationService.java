package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.User;

import java.util.UUID;

public interface ContestParticipationService {
    void participateInContest(User user, UUID id);
}
