package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Rank;
import com.telerikacademy.web.photocontest.repositories.RankRepository;
import com.telerikacademy.web.photocontest.services.contracts.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final RankRepository rankRepository;

    @Override
    public Rank getRankById(UUID id) {
        return rankRepository.getReferenceById(id);
    }

    @Override
    public Rank getRankByName(String name) {
        return rankRepository.findByName(name);
    }
}
