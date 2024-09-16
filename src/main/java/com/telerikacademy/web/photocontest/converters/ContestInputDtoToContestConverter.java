package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.dtos.ContestInput;
import com.telerikacademy.web.photocontest.repositories.PhaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ContestInputDtoToContestConverter implements Converter<ContestInput, Contest> {

    private final PhaseRepository phaseRepository;

    @Override
    public Contest convert(ContestInput dto) {
        return Contest.builder()
                .title(dto.getTitle())
                .category(dto.getCategory())
                .phase(phaseRepository.findByName("Phase 1"))
                .createdAt(LocalDateTime.now())
                .changePhaseTime(LocalDateTime.now())
                .isActive(true)
                .build();
    }
}
