package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.models.Rank;
import com.telerikacademy.web.photocontest.repositories.RankRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.RankService;
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
}
