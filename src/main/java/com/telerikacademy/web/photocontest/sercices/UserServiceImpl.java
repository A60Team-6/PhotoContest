package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.entities.dtos.UserOutputIdDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdateDto;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;



    @Override
    public List<UserOutputDto> getAll(){
        List<User> users = userRepository.findAllByIsActiveTrue();
        return users.stream().map(user -> conversionService.convert(user, UserOutputDto.class)).collect(Collectors.toList());

    }

    @Override
    public UserOutputDto findUserById(UUID userId) {
        User user = userRepository.findByUserIdAndIsActiveTrue(userId);
        if (user == null) {
            throw new EntityNotFoundException("User with ID " + userId + " not found.");
        }
        return conversionService.convert(user, UserOutputDto.class);
    }

    @Override
    public UserOutputDto findUserByUsername(String username) {
        User user = userRepository.findByUsernameAndIsActiveTrue(username);
        return conversionService.convert(user, UserOutputDto.class);
    }

    @Override
    public User findUserByUsernameAuth(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    @Override
    public UserOutputIdDto createUser(UserInputDto userInputDto) {
        User user = conversionService.convert(userInputDto, User.class);
        User existingUser = userRepository.findByUsernameAndIsActiveTrue(user.getUsername());

        if (existingUser != null) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        userRepository.save(user);

       return conversionService.convert(user, UserOutputIdDto.class);
    }

    @Override
    public UserUpdateDto editUser(User user, User userToEdit) {
        User foundUser = userRepository.findByUsernameAndIsActiveTrue(userToEdit.getUsername());
        if (foundUser == null) {
            throw new EntityNotFoundException("User not found or inactive.");
        }

        PermissionHelper.isSameUser(user, userRepository.findByUsernameAndIsActiveTrue(userToEdit.getUsername()), "You can edit only your profile!");

        user.setFirstName(userToEdit.getFirstName());
        user.setLastName(userToEdit.getLastName());
        user.setEmail(userToEdit.getEmail());
        user.setPassword(userToEdit.getPassword());
        user.setProfilePhoto(userToEdit.getProfilePhoto());;

        userRepository.save(user);

        return conversionService.convert(user, UserUpdateDto.class);
    }

    @Override
    public void deactivateUser(UUID userId, User user) {
        PermissionHelper.isSameUser(user, userRepository.findByUsernameAndIsActiveTrue(user.getUsername()), "You can deactivate only yourself!");
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Set<Photo> photos = existingUser.getPhotos();
        for(Photo photo : photos) {
            photo.setIsActive(false);
        }
        existingUser.setIsActive(false);
        userRepository.save(existingUser);
    }
}
