package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserUpdateDtoToUserConverter implements Converter<UserUpdate, User> {

    @Override
    public User convert(UserUpdate user) {

        return User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }
}
