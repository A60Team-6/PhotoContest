package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputId;
import com.telerikacademy.web.photocontest.entities.dtos.UserInput;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutput;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdate;

import java.util.List;
import java.util.UUID;

public interface UserService {


    List<User> getAllUserEntities();

    List<UserOutput> getAll();

    UserOutput findUserById(UUID userId);

    User findUserEntityById(UUID userId);

    UserOutput findUserByUsername(String username);

    User findUserByUsernameAuth(String username);

    UserOutputId createUser(UserInput user);

    UserUpdate editUser(User user, UserUpdate userToEdit);

    void deactivateUser(UUID userId, User user);
}
