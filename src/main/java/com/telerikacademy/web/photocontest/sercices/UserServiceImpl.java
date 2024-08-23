package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<User> getAll(){
        return userRepository.findAllByIsActiveTrue();
    }

    @Override
    public User findUserById(UUID userId) {
        return userRepository.findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    @Override
    public void createUser(User user) {
        User existingUser = userRepository.findByUsernameAndIsActiveTrue(user.getUsername());

        if (existingUser != null) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        userRepository.save(user);
    }


    @Override
    public void editUser(User user, User userToEdit) {
        PermissionHelper.isSameUser(user, userRepository.findByUsernameAndIsActiveTrue(userToEdit.getUsername()), "You can edit only your profile!");

        user.setUsername(userToEdit.getUsername());
        user.setFirstName(userToEdit.getFirstName());
        user.setLastName(userToEdit.getLastName());
        user.setEmail(userToEdit.getEmail());
        user.setPassword(userToEdit.getPassword());
        user.setProfilePhoto(userToEdit.getProfilePhoto());;

        userRepository.save(user);
    }

    @Override
    public void deactivateUser(UUID userId, User user) {
        PermissionHelper.isSameUser(user, userRepository.findByUsernameAndIsActiveTrue(user.getUsername()), "You can deactivate only yourself!");
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setIsActive(false);
        userRepository.save(existingUser);
    }
}
