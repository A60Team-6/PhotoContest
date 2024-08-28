package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Phase;
import com.telerikacademy.web.photocontest.repositories.PhaseRepository;
import com.telerikacademy.web.photocontest.services.contracts.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;

    @Override
    public Phase getPhaseById(UUID id) {
        return phaseRepository.getReferenceById(id);
    }

    @Override
    public Phase getPhaseByName(String name) {
        return phaseRepository.findByName(name);
    }
}
