package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.services.contracts.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private final PhotoService photoService;


    @Override
    public List<User> getAllUserEntities() {
        return userRepository.findAll();
    }

    @Override
    public List<UserOutput> getAll() {
        List<User> users = userRepository.findAllByIsActiveTrue();
        return users.stream().map(user -> conversionService.convert(user, UserOutput.class)).collect(Collectors.toList());

    }

    @Override
    public void invite(User user) {
        user.setIsInvited(true);
        userRepository.save(user);
    }

    @Override
    public void acceptInvitation(User user) {
        user.setIsInvited(false);
        user.setRole(roleService.getRoleByName("Jury"));
        userRepository.save(user);
    }

    @Override
    public void declineInvitation(User user) {
        user.setIsInvited(false);
        userRepository.save(user);
    }

    @Override
    public Page<User> getUsersWithFilters(String username, String firstName, String email, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        String usernameLike = username != null ? "%" + username + "%" : null;
        String firstNameLike = firstName != null ? "%" + firstName + "%" : null;
        String emailLike = email != null ? "%" + email + "%" : null;

        return userRepository.findUsersByMultipleFields(usernameLike, firstNameLike, emailLike, pageable);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> getAllUsersWithJuryRights() {
        List<User> users = userRepository.findAllByIsActiveTrue();
        List<User> jurors = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().getName().equals("Jury") || user.getRole().getName().equals("Organizer")) {
                jurors.add(user);
            }
        }
        return jurors;
    }

    @Override
    public UserOutput findUserById(UUID userId, User authenticatedUser) {
        User user = userRepository.findByUserIdAndIsActiveTrue(userId);
        PermissionHelper.isSameUserOrOrganizer(authenticatedUser, user, "You have not access to this user!");
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
    public User findUserEntityByUsername(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    @Override
    public User findUserByUsernameAuth(String username) {
        return userRepository.findByUsernameAndIsActiveTrue(username);
    }

    @Override
    public UserOutputId createUser(Register register) {
        String hashedPassword = passwordEncoder.encode(register.getPassword());

        User user = User.builder()
                .username(register.getUsername())
                .firstName(register.getFirstName())
                .lastName(register.getLastName())
                .email(register.getEmail())
                .password(hashedPassword)
                .role(roleService.getRoleByName("Junkie"))
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
        List<Photo> photos = photoService.getAllPhotosEntityOfUser(user);
        for (Photo photo : photos) {
            photo.setIsActive(false);
        }
        existingUser.setIsActive(false);
        userRepository.save(existingUser);
    }

    @Override
    public UploadFileOutput uploadPhoto(String username, MultipartFile file) throws IOException {

        if (username == null || file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid file input");
        }

        User user = findUserEntityByUsername(username);

        String photoUrl = cloudinaryService.uploadFile(file);

        if (photoUrl == null || photoUrl.isEmpty()) {
            throw new IOException("Failed to upload photo to Cloudinary");
        }

        user.setProfilePhoto(photoUrl);

        userRepository.save(user);

        return UploadFileOutput.builder().message("Photo uploaded and URL updated successfully!").build();
    }
}
