package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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
    private final ConversionService conversionService;

    @GetMapping
    public List<ContestOutput> getAll() {
        return contestService.getAll();
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
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(contestService.createContest(contestInput, user));
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContest(@PathVariable UUID id,@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            contestService.deactivateContest(id, user);
            return ResponseEntity.ok("Deleted contest successfully");
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
