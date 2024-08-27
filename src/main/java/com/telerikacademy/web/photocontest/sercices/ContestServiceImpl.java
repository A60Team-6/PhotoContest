package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Scheduled(fixedRate = 60000)
    public void updateContestPhases() {
        List<Contest> activeContests = contestRepository.findAllByIsActiveTrue();
        for (Contest contest : activeContests) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(contest.getChangePhaseTime())) {
                changePhase(contest);
            }
        }
    }

    private void changePhase(Contest contest) {
        String currentPhase = contest.getPhase().getName();
        LocalDateTime now = LocalDateTime.now();

        if ("Phase 1".equals(currentPhase)) {
            contest.setPhase(new Phase(UUID.fromString("24bdacbf-623c-11ef-97e5-50ebf6c3d3f0"), "Phase 2"));
            contest.setChangePhaseTime(now.plusHours(2));
        } else if ("Phase 2".equals(currentPhase)) {
            contest.setPhase(new Phase(UUID.fromString("24bdad85-623c-11ef-97e5-50ebf6c3d3f0"), "Finished"));
            contest.setIsActive(false);
        }

        contest.setChangePhaseTime(now);
        contestRepository.save(contest);
    }

}
