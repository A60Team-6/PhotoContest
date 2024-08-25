package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ContestServiceImpl implements ContestService {


    private final ContestRepository contestRepository;


    @Override
    public List<Contest> getAll(){
        return contestRepository.findAllByIsActiveTrue();
    }

    @Override
    public Contest findContestById(UUID contestId) {
        return contestRepository.findByContestIdAndIsActiveTrue(contestId);
    }

    @Override
    public Contest findContestByTitle(String title) {
        return contestRepository.findByTitleAndIsActiveTrue(title);
    }

    @Override
    public void createContest(Contest contest, User user) {
        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByTitle(contest.getTitle());

        if (existingContest != null) {
            throw new DuplicateEntityException("Contest", "title", contest.getTitle());
        }

        contestRepository.save(contest);
    }

    @Override
    public void deactivateContest(UUID contestId, User user) {
        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByContestIdAndIsActiveTrue(contestId);

        if (existingContest == null) {
            throw new EntityNotFoundException("This contest does not exist");
        }

        existingContest.setIsActive(false);
        contestRepository.save(existingContest);
    }

}
