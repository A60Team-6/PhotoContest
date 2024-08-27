package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContestToContestOutputDtoConverter implements Converter<Contest, ContestOutputDto> {


    @Override
    public ContestOutputDto convert(Contest contest) {

        return ContestOutputDto.builder()
                .title(contest.getTitle())
                .category(contest.getCategory())
                .phase(contest.getPhase())
                .build();
    }
}
