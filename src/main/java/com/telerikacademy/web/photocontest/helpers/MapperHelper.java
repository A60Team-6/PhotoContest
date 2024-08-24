package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.models.*;
import com.telerikacademy.web.photocontest.models.dtos.*;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MapperHelper {

    private static final String RANG_JUNKIE_ID = "9162fbf0-6190-11ef-97e5-50ebf6c3d3f0";
    private static final String ROLE_USER_ID = "91607ef9-6190-11ef-97e5-50ebf6c3d3f0";
    public static final String UUID = "24bda80f-623c-11ef-97e5-50ebf6c3d3f0";
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
        UUID rankId = java.util.UUID.fromString(RANG_JUNKIE_ID);
        Rank rank = new Rank(rankId, "Junkie");
        user.setRank(rank);
        UUID roleId = java.util.UUID.fromString(ROLE_USER_ID);
        Role role = new Role(roleId, "User");
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);
        return user;
    }

    public User updateUserFromUserInputDto(UserUpdateDto userUpdateDto) {
        User user = new User();
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setEmail(userUpdateDto.getEmail());
        user.setPassword(userUpdateDto.getPassword());
        user.setProfilePhoto(userUpdateDto.getProfilePicture());
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

    public Photo createPhotoFromPhotoInputDto(PhotoInputDto photoInputDto) {
        Photo photo = new Photo();
        photo.setTitle(photoInputDto.getTitle());
        photo.setStory(photoInputDto.getStory());
        photo.setPhotoUrl(photoInputDto.getPhotoUrl());
        photo.setCreatedAt(LocalDateTime.now());
        photo.setIsActive(true);
        return photo;
    }

    public ContestOutputDto changeFromContestToContestOutDto(Contest contest) {
        ContestOutputDto contestOutputDto = new ContestOutputDto();
        contestOutputDto.setTitle(contest.getTitle());
        contestOutputDto.setCategory(contest.getCategory());
        contestOutputDto.setPhase(contest.getPhase());
        return contestOutputDto;
    }

    public Contest createContestFromContestInputDto(ContestInputDto contestInputDto, User user){
        Contest contest = new Contest();
        contest.setTitle(contestInputDto.getTitle());
        contest.setCategory(contestInputDto.getCategory());
        contest.setPhotoUrl(contestInputDto.getCoverPhotoUrl());
        contest.setOrganizer(user);
        LocalDateTime createdAt = LocalDateTime.now();
        contest.setCreatedAt(createdAt);
        contest.setChangePhaseTime(createdAt.plusHours(2));;
        UUID phaseUUIDID = java.util.UUID.fromString(UUID);
        Phase phase = new Phase(phaseUUIDID, "Phase 1");
        contest.setPhase(phase);
        contest.setIsActive(true);
        return contest;
    }
}
