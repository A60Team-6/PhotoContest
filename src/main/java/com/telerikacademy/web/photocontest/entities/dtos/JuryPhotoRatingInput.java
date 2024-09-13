package com.telerikacademy.web.photocontest.entities.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuryPhotoRatingInput {

    @NotNull
    private UUID photoId;

    @NotNull
    private UUID userId;

    @Min(0)
    @Max(10)
    private int score;

    @NotNull
    private String comment;

    private Boolean categoryMatch;
}
