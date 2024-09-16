package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoOutput;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.ContestService;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contests")
public class ContestRestController {

    private final ContestService contestService;
    private final AuthenticationHelper authenticationHelper;
    private final PhotoService photoService;

    @GetMapping
    public List<ContestOutput> getAll() {
        return contestService.getAllActive();
    }

    @GetMapping("/all/photos/contest/{contestId}")
    public ResponseEntity<List<PhotoOutput>> getAllPhotosOfContest(@PathVariable UUID contestId) {
        Contest contest = contestService.findContestEntityById(contestId);
        return ResponseEntity.ok(photoService.getAllPhotosOfContest(contest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestOutput> getContestById(@PathVariable UUID id) {
        return ResponseEntity.ok(contestService.findContestById(id));
    }

    @GetMapping("/title")
    public ResponseEntity<ContestOutput> getContestByTitle(@RequestParam String title) {
        return ResponseEntity.ok(contestService.findContestByTitle(title));
    }

    @PostMapping
    public ResponseEntity<ContestOutputId> createContest(@RequestHeader HttpHeaders headers, @Valid @RequestBody ContestInput contestInput) {
        User user = authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(contestService.createContest(contestInput, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContest(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        contestService.deactivateContest(id, user);
        return ResponseEntity.ok("Deleted contest successfully");
    }
}
