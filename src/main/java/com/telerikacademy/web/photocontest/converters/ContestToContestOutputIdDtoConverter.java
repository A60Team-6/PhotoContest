package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContestToContestOutputIdDtoConverter implements Converter<Contest, ContestOutputId> {

    @Override
    public ContestOutputId convert(Contest contest) {

        return ContestOutputId.builder()
                .contestId(contest.getContestId().toString()).build();
    }
}
