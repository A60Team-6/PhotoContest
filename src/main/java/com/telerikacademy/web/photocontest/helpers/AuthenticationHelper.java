package com.telerikacademy.web.photocontest.helpers;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";

    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.findUserByUsernameAuth(username);

//            if (!user.getPassword().equals(password)) {
//                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
//            }
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new AuthorizationException(WRONG_USERNAME_OR_PASSWORD);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }
}
