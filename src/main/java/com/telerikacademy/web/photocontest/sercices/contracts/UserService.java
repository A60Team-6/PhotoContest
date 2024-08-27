package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputIdDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserService {


    List<UserOutputDto> getAll();

    UserOutputDto findUserById(UUID userId);

    UserOutputDto findUserByUsername(String username);

    User findUserByUsernameAuth(String username);

    UserOutputIdDto createUser(UserInputDto user);

    UserUpdateDto editUser(User user, User userToEdit);

    void deactivateUser(UUID userId, User user);
}
