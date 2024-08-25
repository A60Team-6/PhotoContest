package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestParticipationService;
import com.telerikacademy.web.photocontest.sercices.contracts.ContestService;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contestsParticipation")
public class ContestParticipationRestController {

    private final ContestParticipationService contestParticipationService;
    private final AuthenticationHelper authenticationHelper;
    private final ContestService contestService;
    private final UserService userService;


    @PostMapping("/{contestId}")
    public void participateInContest(@PathVariable UUID contestId, @RequestHeader HttpHeaders httpHeaders) {
        User user =  authenticationHelper.tryGetUser(httpHeaders);
        contestParticipationService.participateInContest(user, contestId);
    }


}
