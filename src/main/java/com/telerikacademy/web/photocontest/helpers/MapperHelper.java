package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.entities.*;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
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
    private final PhotoRepository photoRepository;


    public User createUserFromUserInputDto(UserInput userInput) {
        User user = new User();
        user.setUsername(userInput.getUsername());
        user.setFirstName(userInput.getFirstName());
        user.setLastName(userInput.getLastName());
        user.setEmail(userInput.getEmail());
        user.setPassword(userInput.getPassword());
        user.setProfilePhoto(userInput.getProfilePicture());
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

    public User updateUserFromUserInputDto(UserUpdate userUpdate) {
        User user = new User();
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setEmail(userUpdate.getEmail());
        user.setPassword(userUpdate.getPassword());
        user.setProfilePhoto(userUpdate.getProfilePicture());
        return user;
    }

    public UserOutput changeFromUserToUserOutputDto(User user) {
        UserOutput userOutput = new UserOutput();
        userOutput.setUsername(user.getUsername());
        userOutput.setFirstName(user.getFirstName());
        userOutput.setLastName(user.getLastName());
        userOutput.setEmail(user.getEmail());
        userOutput.setProfilePicture(user.getProfilePhoto());
        return userOutput;
    }

//    public Photo createPhotoFromPhotoInputDto(PhotoInput photoInputDto) {
//        return Photo.builder()
//                .title(photoInputDto.getTitle())
//                .story(photoInputDto.getStory())
//                .photoUrl(photoInputDto.getPhotoUrl())
//                .createdAt(LocalDateTime.now())
//                .isActive(true)
//                .build();
//    }

    public Photo updatePhotoFromDto(PhotoInput photoInputDto, Photo photo) {
        photo.setTitle(photoInputDto.getTitle());
        photo.setStory(photoInputDto.getStory());
        photo.setPhotoUrl(photoInputDto.getPhotoUrl());

        return photo;
    }


    public PhotoOutput changeFromPhotoToPhotoOutDto(Photo photo) {
        return PhotoOutput.builder()
                .title(photo.getTitle())
                .story(photo.getStory())
                .photoUrl(photo.getPhotoUrl())
                .contest(String.valueOf(photo.getContest()))
                .user(photo.getUser().getUsername())
                .createdAt(photo.getCreatedAt())
                .build();
    }

    public ContestOutput changeFromContestToContestOutDto(Contest contest) {
        ContestOutput contestOutput = new ContestOutput();
        contestOutput.setTitle(contest.getTitle());
        contestOutput.setCategory(contest.getCategory());
        contestOutput.setPhase(contest.getPhase());
        return contestOutput;
    }

    public Contest createContestFromContestInputDto(ContestInput contestInput, User user) {
        Contest contest = new Contest();
        contest.setTitle(contestInput.getTitle());
        contest.setCategory(contestInput.getCategory());
        contest.setPhotoUrl(contestInput.getCoverPhotoUrl());
        contest.setOrganizer(user);
        LocalDateTime createdAt = LocalDateTime.now();
        contest.setCreatedAt(createdAt);
        contest.setChangePhaseTime(createdAt.plusHours(2));
        ;
        UUID phaseUUIDID = java.util.UUID.fromString(UUID);
        Phase phase = new Phase(phaseUUIDID, "Phase 1");
        contest.setPhase(phase);
        contest.setIsActive(true);
        return contest;
    }

    public JuryPhotoRating toJuryPhotoRating(JuryPhotoRatingInputDto dto, Photo photo, User user) {
        return JuryPhotoRating.builder()
                .photo(photo)
                .jury(user)
                .score(dto.getScore())
                .comment(dto.getComment())
                .categoryMatch(dto.getCategoryMatch())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
    }

    public JuryPhotoRatingOutputDto toJuryPhotoRatingOutputDto(JuryPhotoRating juryPhotoRating) {
        return JuryPhotoRatingOutputDto.builder()
                .id(juryPhotoRating.getId())
                .photoId(juryPhotoRating.getPhoto().getId())
                .userId(juryPhotoRating.getJury().getUserId())
                .score(juryPhotoRating.getScore())
                .comment(juryPhotoRating.getComment())
                .categoryMatch(juryPhotoRating.getCategoryMatch())
                .reviewDate(juryPhotoRating.getCreatedAt())
                .isActive(juryPhotoRating.getIsActive())
                .build();
    }
}
