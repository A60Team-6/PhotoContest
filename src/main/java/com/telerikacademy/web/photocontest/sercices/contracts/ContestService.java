package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputIdDto;

import java.util.List;
import java.util.UUID;

public interface ContestService {
    List<Contest> getAll();

    ContestOutputDto findContestById(UUID contestId);

    ContestOutputDto findContestByTitle(String title);

    ContestOutputIdDto createContest(Contest contest, User user);

    void deactivateContest(UUID contestId, User user);
}
