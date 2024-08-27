package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserToUserUpdateDtoConverter implements Converter<User, UserUpdateDto> {

    @Override
    public UserUpdateDto convert(User user) {

        return UserUpdateDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .profilePicture(user.getProfilePhoto())
                .build();
    }
}
