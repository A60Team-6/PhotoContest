package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.Phase;

import java.util.UUID;

public interface PhaseService {

    Phase getPhaseById(UUID id);

    Phase getPhaseByName(String name);
}
