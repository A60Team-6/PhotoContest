package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserToUserIdDtoConverter implements Converter<User, UserOutputId> {

    @Override
    public UserOutputId convert(User user) {

        return UserOutputId.builder()
                .userId(user.getUserId().toString()).build();
    }
}
