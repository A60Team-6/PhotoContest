package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserToUserOutputDtoConverter implements Converter<User, UserOutput> {

    @Override
    public UserOutput convert(User user) {
        return UserOutput.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePhoto())
                .build();
    }
}
