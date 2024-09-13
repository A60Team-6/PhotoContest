package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.entities.dtos.FinishedContestAntItsWinner;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ContestService {
    List<ContestOutput> getAllActive();

    List<FinishedContestAntItsWinner> getAllUnActive();

    List<Contest> getAllActiveContestInPhase1();

    List<Contest> getAllActiveContestInPhase2();

    List<ContestOutput> getAllContests();

    Page<Contest> getContestsWithFilters(String title, String category, String phase, int page, int size, String sortBy, String sortDirection);

    ContestOutput findContestById(UUID contestId);

    Contest findContestEntityById(UUID contestId);

    ContestOutput findContestByTitle(String title);

    ContestOutputId createContest(ContestInput contestInput, User user);

    void deactivateContest(UUID contestId, User user);
}
