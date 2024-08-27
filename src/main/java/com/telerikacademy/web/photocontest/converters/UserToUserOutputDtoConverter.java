package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Rank;
import com.telerikacademy.web.photocontest.entities.Role;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserToUserOutputDtoConverter implements Converter<User, UserOutputDto> {

    @Override
    public UserOutputDto convert(User user) {

        return UserOutputDto.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePhoto())
                .build();
    }
}
