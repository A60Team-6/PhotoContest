package com.telerikacademy.web.photocontest.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOutput {

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String profilePicture;
}
