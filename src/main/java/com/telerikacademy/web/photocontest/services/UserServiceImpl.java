package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.contracts.CloudinaryService;
import com.telerikacademy.web.photocontest.services.contracts.RankService;
import com.telerikacademy.web.photocontest.services.contracts.RoleService;
import com.telerikacademy.web.photocontest.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final RoleService roleService;
    private final RankService rankService;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;



    @Override
    public List<User> getAllUserEntities(){
        return userRepository.findAll();
    }


    @Override
    public List<UserOutput> getAll(){
        List<User> users = userRepository.findAllByIsActiveTrue();
        return users.stream().map(user -> conversionService.convert(user, UserOutput.class)).collect(Collectors.toList());

    }

    @Override
    public UserOutput findUserById(UUID userId) {
        User user = userRepository.findByUserIdAndIsActiveTrue(userId);
        if (user == null) {
            throw new EntityNotFoundException("User with ID " + userId + " not found.");
        }
        return conversionService.convert(user, UserOutput.class);
    }

    @Override
    public User findUserEntityById(UUID userId) {
        User user = userRepository.findByUserIdAndIsActiveTrue(userId);
        if (user == null) {
            throw new EntityNotFoundException("User with ID " + userId + " not found.");
        }
        return user;
    }

    @Override
    public UserOutput findUserByUsername(String username) {
        User user = userRepository.findByUsernameAndIsActiveTrue(username);
        return conversionService.convert(user, UserOutput.class);
    }

    @Override
    public User findUserByUsernameAuth(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    @Override
    public UserOutputId createUser(UserInput userInput) {
        String hashedPassword = passwordEncoder.encode(userInput.getPassword());

        User user = User.builder()
                .username(userInput.getUsername())
                .firstName(userInput.getFirstName())
                .lastName(userInput.getLastName())
                .email(userInput.getEmail())
                .password(hashedPassword)
                .role(roleService.getRoleByName("User"))
                .rank(rankService.getRankByName("Junkie"))
                .build();

        User existingUser = userRepository.findByUsernameAndIsActiveTrue(user.getUsername());
        User existingEmail = userRepository.findByEmailAndIsActiveTrue(user.getEmail());

        if (existingUser != null) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        if (existingEmail != null) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.save(user);

       return conversionService.convert(user, UserOutputId.class);
    }

    @Override
    public UserUpdate editUser(User user, UserUpdate userUpdate) {


        if (!passwordEncoder.matches(userUpdate.getPassword(), user.getPassword())) {
            String hashedPassword = passwordEncoder.encode(userUpdate.getPassword());
            user.setPassword(hashedPassword);
        }

        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setEmail(userUpdate.getEmail());

        userRepository.save(user);

        return conversionService.convert(user, UserUpdate.class);
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

    @Override
    public UploadFileOutput uploadPhoto(String id, MultipartFile file) throws IOException {

        if (id == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid file input");
        }

        User user = findUserEntityById(UUID.fromString(id));

        String photoUrl = cloudinaryService.uploadFile(file);

        if (photoUrl == null || photoUrl.isEmpty()) {
            throw new IOException("Failed to upload photo to Cloudinary");
        }

        user.setProfilePhoto(photoUrl);

        userRepository.save(user);

        return UploadFileOutput.builder().message("Photo uploaded and URL updated successfully!").build();
    }
}
