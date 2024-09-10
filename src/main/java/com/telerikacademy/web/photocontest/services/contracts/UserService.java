package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {


    List<User> getAllUserEntities();

    List<UserOutput> getAll();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> getAllUsersWithJuryRights();

    UserOutput findUserById(UUID userId, User authenticatedUser);

    User findUserEntityById(UUID userId);

    UserOutput findUserByUsername(String username);

    User findUserEntityByUsername(String username);

    User findUserByUsernameAuth(String username);

  //  UserOutputId createUser(UserInput user);

    UserOutputId createUser(Register register);

    UserUpdate editUser(User user, UserUpdate userToEdit);

    void deactivateUser(UUID userId, User user);

//    void uploadPhoto(UploadFileInput uploadFileInput);

    UploadFileOutput uploadPhoto(String id, MultipartFile file) throws IOException;
}

