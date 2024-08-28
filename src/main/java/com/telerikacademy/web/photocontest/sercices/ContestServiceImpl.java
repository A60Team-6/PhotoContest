package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContestServiceImpl implements ContestService {


    private final ConversionService conversionService;
    private final ContestRepository contestRepository;
    private final PhaseService phaseService;


    @Override
    public List<ContestOutput> getAll(){
        List<Contest> contests = contestRepository.findAllByIsActiveTrue();
        return contests.stream().map(contest -> conversionService.convert(contest, ContestOutput.class)).collect(Collectors.toList());
    }

    @Override
    public ContestOutput findContestById(UUID contestId) {
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(contestId);
        if (contest == null) {
            throw new EntityNotFoundException("Contest with ID " + contestId + " not found.");
        }
        return conversionService.convert(contest, ContestOutput.class);
    }

    @Override
    public Contest findContestEntityById(UUID contestId){
        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(contestId);
        if (contest == null) {
            throw new EntityNotFoundException("Contest with ID " + contestId + " not found.");
        }
        return contest;
    }

    @Override
    public ContestOutput findContestByTitle(String title) {
        Contest contest = contestRepository.findByTitleAndIsActiveTrue(title);
        return conversionService.convert(contest, ContestOutput.class);
    }

    @Override
    public ContestOutputId createContest(ContestInput contestInput, User user) {
       // Contest contest = conversionService.convert(contestInput, Contest.class);
        Contest contest = Contest.builder()
                .title(contestInput.getTitle())
                .category(contestInput.getCategory())
                .phase(phaseService.getPhaseByName("Phase 1"))
                .createdAt(LocalDateTime.now())
                .changePhaseTime(LocalDateTime.now())
                .isActive(true)
                .build();

        if (contest == null){
            throw new EntityNotFoundException("Contest not found");
        }
        contest.setOrganizer(user);

        PermissionHelper.isOrganizer(user, "You are not permitted for this action!");
        Contest existingContest = contestRepository.findByTitleAndIsActiveTrue(contest.getTitle());

        if (existingContest != null) {
            throw new DuplicateEntityException("Contest", "title", contest.getTitle());
        }

        contestRepository.save(contest);
        return conversionService.convert(contest, ContestOutputId.class);
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
        Phase phase2 = phaseService.getPhaseByName("Phase 2");
        if (phase2 == null) {
            throw new RuntimeException("Phase 2 not found in the database");
        }

        if ("Phase 1".equals(currentPhase)) {
            contest.setPhase(phase2);
            contest.setChangePhaseTime(now.plusMinutes(3));
            System.out.println("Contest moved to Phase 2");
        } else if ("Phase 2".equals(currentPhase)) {
            contest.setPhase(phaseService.getPhaseByName("Finished"));
            contest.setIsActive(false);
            System.out.println("Contest finished");
        }

        contest.setChangePhaseTime(now);
        contestRepository.save(contest);
    }

}
