package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputIdDto;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.PhaseRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestService;
import com.telerikacademy.web.photocontest.sercices.contracts.PhaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ContestServiceImpl implements ContestService {


    private final ConversionService conversionService;
    private final ContestRepository contestRepository;
    private final PhaseRepository phaseRepository;


    @Override
    public List<Contest> getAll(){
        return contestRepository.findAllByIsActiveTrue();
    }

    @Override
    public ContestOutputDto findContestById(UUID contestId) {
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(contestId);
        if (contest == null) {
            throw new EntityNotFoundException("Contest with ID " + contestId + " not found.");
        }
        return conversionService.convert(contest, ContestOutputDto.class);
    }

    @Override
    public ContestOutputDto findContestByTitle(String title) {
        Contest contest = contestRepository.findByTitleAndIsActiveTrue(title);
        return conversionService.convert(contest, ContestOutputDto.class);
    }

    @Override
    public ContestOutputIdDto createContest(Contest contest, User user) {
        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByTitle(contest.getTitle());

        if (existingContest != null) {
            throw new DuplicateEntityException("Contest", "title", contest.getTitle());
        }

        contestRepository.save(contest);
        return conversionService.convert(contest, ContestOutputIdDto.class);
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

    @Scheduled(fixedRate = 1000)
    public void updateContestPhases() {
        System.out.println("Scheduled task running");
        List<Contest> activeContests = contestRepository.findAllByIsActiveTrue();
        if(activeContests.isEmpty()) {
            System.out.println("No active contests found");
        }
        for (Contest contest : activeContests) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(contest.getChangePhaseTime())) {
                System.out.println("Changing phase for contest: " + contest.getTitle());
                changePhase(contest);
            }
        }
    }

    private void changePhase(Contest contest) {
        String currentPhase = contest.getPhase().getName();
        LocalDateTime now = LocalDateTime.now();
        Phase phase2 = phaseRepository.findByName("Phase 2");
        if (phase2 == null) {
            throw new RuntimeException("Phase 2 not found in the database");
        }

        if ("Phase 1".equals(currentPhase)) {
            contest.setPhase(phase2);
            contest.setChangePhaseTime(now.plusSeconds(10));
            System.out.println("Contest moved to Phase 2");
        } else if ("Phase 2".equals(currentPhase)) {
            contest.setPhase(phaseRepository.findByName("Finished"));
            contest.setIsActive(false);
            System.out.println("Contest finished");
        }

        contest.setChangePhaseTime(now);
        contestRepository.save(contest);
    }

}
