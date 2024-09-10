package com.telerikacademy.web.photocontest.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {

    @NotNull(message = "Username can not be empty!")
    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols!")
    private String username;

    @Size(min = 8, max = 32, message = "Password should be between 8 and 32 symbols!")
    @NotNull(message = "Password can not be empty!")
    private String password;


}
