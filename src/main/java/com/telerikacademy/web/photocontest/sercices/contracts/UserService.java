package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {


    List<User> getAll();

    User findUserById(UUID userId);

    User findUserByUsername(String username);

    void createUser(User user);

    void editUser(User user, User userToEdit);

    void deactivateUser(UUID userId, User user);
}
