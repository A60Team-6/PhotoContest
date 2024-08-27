package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserUpdateDtoToUserConverter implements Converter<UserUpdateDto, User> {

    @Override
    public User convert(UserUpdateDto user) {

        return User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .email(user.getEmail())
                .profilePhoto(user.getProfilePicture())
                .build();
    }
}
