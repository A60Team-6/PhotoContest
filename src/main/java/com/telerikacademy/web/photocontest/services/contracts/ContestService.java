package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;

import java.util.List;
import java.util.UUID;

public interface ContestService {
    List<ContestOutput> getAll();

    ContestOutput findContestById(UUID contestId);

    Contest findContestEntityById(UUID contestId);

    ContestOutput findContestByTitle(String title);

    ContestOutputId createContest(ContestInput contestInput, User user);

    void deactivateContest(UUID contestId, User user);
}
