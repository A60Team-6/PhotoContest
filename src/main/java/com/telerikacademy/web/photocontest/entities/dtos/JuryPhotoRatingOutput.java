package com.telerikacademy.web.photocontest.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuryPhotoRatingOutput {
    private UUID id;
    private UUID photoId;
    private UUID userId;
    private int score;
    private String comment;
    private Boolean categoryMatch;
    private LocalDateTime reviewDate;
    private Boolean isActive;
}
