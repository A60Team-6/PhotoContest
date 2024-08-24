package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.ContestInputDto;
import com.telerikacademy.web.photocontest.models.dtos.ContestOutputDto;
import com.telerikacademy.web.photocontest.models.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.models.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contests")
public class ContestRestController {

    private final ContestService contestService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping
    public List<Contest> getAll() {
        return contestService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestOutputDto> getContestById(@PathVariable UUID id) {
        Contest contest = contestService.findContestById(id);
        return ResponseEntity.ok(mapperHelper.changeFromContestToContestOutDto(contest));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<ContestOutputDto> getContestByTitle(@PathVariable String title) {
        Contest contest = contestService.findContestByTitle(title);
        return ResponseEntity.ok(mapperHelper.changeFromContestToContestOutDto(contest));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody ContestInputDto contestInputDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Contest contest = mapperHelper.createContestFromContestInputDto(contestInputDto, user);
            contestService.createContest(contest, user);
            return ResponseEntity.ok(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
