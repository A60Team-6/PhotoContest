package com.telerikacademy.web.photocontest;
import com.telerikacademy.web.photocontest.entities.Rank;
import com.telerikacademy.web.photocontest.repositories.RankRepository;
import com.telerikacademy.web.photocontest.services.RankServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RankServiceImplTest {

    @Mock
    private RankRepository rankRepository;

    @InjectMocks
    private RankServiceImpl rankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRankById_ShouldReturnRank_WhenRankExists() {
        // Arrange
        UUID rankId = UUID.randomUUID();
        Rank rank = new Rank();
        rank.setRankId(rankId);

        when(rankRepository.getReferenceById(rankId)).thenReturn(rank);

        // Act
        Rank result = rankService.getRankById(rankId);

        // Assert
        assertNotNull(result);
        assertEquals(rankId, result.getRankId());
        verify(rankRepository, times(1)).getReferenceById(rankId);
    }

    @Test
    void testGetRankById_ShouldReturnNull_WhenRankDoesNotExist() {
        // Arrange
        UUID rankId = UUID.randomUUID();
        when(rankRepository.getReferenceById(rankId)).thenReturn(null);

        // Act
        Rank result = rankService.getRankById(rankId);

        // Assert
        assertNull(result);
        verify(rankRepository, times(1)).getReferenceById(rankId);
    }

    @Test
    void testGetRankByName_ShouldReturnRank_WhenRankExists() {
        // Arrange
        String rankName = "Master";
        Rank rank = new Rank();
        rank.setName(rankName);

        when(rankRepository.findByName(rankName)).thenReturn(rank);

        // Act
        Rank result = rankService.getRankByName(rankName);

        // Assert
        assertNotNull(result);
        assertEquals(rankName, result.getName());
        verify(rankRepository, times(1)).findByName(rankName);
    }

    @Test
    void testGetRankByName_ShouldReturnNull_WhenRankDoesNotExist() {
        // Arrange
        String rankName = "NonExistentRank";
        when(rankRepository.findByName(rankName)).thenReturn(null);

        // Act
        Rank result = rankService.getRankByName(rankName);

        // Assert
        assertNull(result);
        verify(rankRepository, times(1)).findByName(rankName);
    }
}
