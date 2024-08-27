package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputIdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserToUserIdDtoConverter implements Converter<User, UserOutputIdDto> {

    @Override
    public UserOutputIdDto convert(User user) {

        return UserOutputIdDto.builder()
                .userId(user.getUserId().toString()).build();
    }
}
