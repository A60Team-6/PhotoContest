package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.User;

import java.util.UUID;

public interface ContestParticipationService {
    void participateInContest(User user, UUID id);

    void uploadPhotoToTheContestCompetition(UUID contestParticipationId, String photoUrl);
}
