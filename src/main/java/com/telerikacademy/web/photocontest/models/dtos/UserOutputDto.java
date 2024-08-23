package com.telerikacademy.web.photocontest.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutputDto {


    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String profilePicture;
}
