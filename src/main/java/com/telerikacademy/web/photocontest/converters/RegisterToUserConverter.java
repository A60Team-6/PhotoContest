package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.Register;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegisterToUserConverter implements Converter<Register, User> {

    @Override
    public User convert(Register register) {

        return User.builder()
                .username(register.getUsername())
                .firstName(register.getFirstName())
                .lastName(register.getLastName())
                .email(register.getEmail())
                .password(register.getPassword())
                .build();
    }

}
