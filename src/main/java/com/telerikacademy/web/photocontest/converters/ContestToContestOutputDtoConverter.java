package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.dtos.ContestOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ContestToContestOutputDtoConverter implements Converter<Contest, ContestOutput> {

    @Override
    public ContestOutput convert(Contest contest) {
        return ContestOutput.builder()
                .title(contest.getTitle())
                .category(contest.getCategory())
                .phase(contest.getPhase())
                .build();
    }
}
