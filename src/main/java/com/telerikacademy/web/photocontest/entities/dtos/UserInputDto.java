package com.telerikacademy.web.photocontest.entities.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDto {

    @NotNull(message = "Username can not be empty!")
    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols!")
    private String username;

    @NotNull(message = "First name can not be empty!")
    @Size(min = 3, max = 32, message = "First name should be between 3 and 32 symbols!")
    private String firstName;

    @NotNull(message = "Last name can not be empty!")
    @Size(min = 3, max = 32, message = "Last name should be between 3 and 32 symbols!")
    private String lastName;

    @NotNull(message = "Email can not be null!")
    @Email(message = "Email is invalid!")
    private String email;

    @NotNull(message = "Password can not be empty!")
    @Size(min = 8)
    private String password;

    @Size(max = 256, message = "Profile picture is too big")
    private String profilePicture;


}
