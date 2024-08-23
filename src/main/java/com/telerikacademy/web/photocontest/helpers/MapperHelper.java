package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.models.Rank;
import com.telerikacademy.web.photocontest.models.Role;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.models.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MapperHelper {

    private final UserService userService;
    private final UserRepository userRepository;


    public User createUserFromUserInputDto(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.getUsername());
        user.setFirstName(userInputDto.getFirstName());
        user.setLastName(userInputDto.getLastName());
        user.setEmail(userInputDto.getEmail());
        user.setPassword(userInputDto.getPassword());
        user.setProfilePhoto(userInputDto.getProfilePicture());
        user.setPoints(0);
        String uuidString = "9162fbf0-6190-11ef-97e5-50ebf6c3d3f0";
        UUID rankId = UUID.fromString(uuidString);
        Rank rank = new Rank(rankId, "Junkie");
        user.setRank(rank);
        String uuidStringRole = "91607ef9-6190-11ef-97e5-50ebf6c3d3f0";
        UUID roleId = UUID.fromString(uuidStringRole);
        Role role = new Role(roleId, "User");
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);
        return user;
    }

    public UserOutputDto changeFromUserToUserOutputDto(User user) {
        UserOutputDto userOutputDto = new UserOutputDto();
        userOutputDto.setUsername(user.getUsername());
        userOutputDto.setFirstName(user.getFirstName());
        userOutputDto.setLastName(user.getLastName());
        userOutputDto.setEmail(user.getEmail());
        userOutputDto.setProfilePicture(user.getProfilePhoto());
        return userOutputDto;
    }
}
