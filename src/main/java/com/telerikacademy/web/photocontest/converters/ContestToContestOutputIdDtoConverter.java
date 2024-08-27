package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputIdDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContestToContestOutputIdDtoConverter implements Converter<Contest, ContestOutputIdDto> {

    @Override
    public ContestOutputIdDto convert(Contest contest) {

        return ContestOutputIdDto.builder()
                .contestId(contest.getContestId().toString()).build();
    }
}
