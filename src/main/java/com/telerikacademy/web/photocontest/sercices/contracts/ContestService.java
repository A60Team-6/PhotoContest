package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;

import java.util.List;
import java.util.UUID;

public interface ContestService {
    List<Contest> getAll();

    Contest findContestById(UUID contestId);

    Contest findContestByTitle(String title);

    void createContest(Contest contest, User user);
}
