package com.telerikacademy.web.photocontest.entities.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestInput {

    @NotNull(message = "Title can not be null!")
    @Size(min = 3, max = 32, message = "Title should be between 3 and 32 symbols!")
    private String title;

    @NotNull(message = "Category can not be null!")
    @Size(min = 3, max = 32, message = "Category should be between 3 and 32 symbols!")
    private String category;

    @Size(max = 255, message = "Cover photo url should be no more than 255 symbols!")
    private String coverPhotoUrl;


}
