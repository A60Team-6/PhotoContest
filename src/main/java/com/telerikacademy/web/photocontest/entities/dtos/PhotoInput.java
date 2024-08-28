package com.telerikacademy.web.photocontest.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoInput {

    @NotNull(message = "Title can not be empty!")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotNull(message = "Story can not be empty!")
    private String story;

    @NotNull(message = "Contest cannot be empty")
    private String contestId;
}
