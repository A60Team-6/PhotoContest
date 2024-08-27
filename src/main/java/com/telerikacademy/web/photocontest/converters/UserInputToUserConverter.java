package com.telerikacademy.web.photocontest.converters;

import com.telerikacademy.web.photocontest.entities.Rank;
import com.telerikacademy.web.photocontest.entities.Role;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.repositories.RankRepository;
import com.telerikacademy.web.photocontest.repositories.RoleRepository;
import com.telerikacademy.web.photocontest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserInputToUserConverter implements Converter<UserInputDto, User> {

    private final RoleRepository roleRepository;
    private final RankRepository rankRepository;


    @Override
    public User convert(UserInputDto dto) {
        Role userRole = roleRepository.findByName("User");
        Rank userRank = rankRepository.findByName("Junkie");

        return User.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .profilePhoto(dto.getProfilePicture())
                .role(userRole)
                .rank(userRank)
                .points(0)
                .isActive(true)
                .build();
    }
}
