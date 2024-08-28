package com.telerikacademy.web.photocontest.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoUpdate {

    @NotNull(message = "Title can not be empty!")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotNull(message = "Story can not be empty!")
    @Size(max = 500, message = "Story must be less than 500 characters")
    private String story;

}
