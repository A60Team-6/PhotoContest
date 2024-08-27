package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.converters.UserInputToUserConverter;
import com.telerikacademy.web.photocontest.converters.UserToUserIdDtoConverter;
import com.telerikacademy.web.photocontest.converters.UserToUserOutputDtoConverter;
import com.telerikacademy.web.photocontest.converters.UserUpdateDtoToUserConverter;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputIdDto;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.UserInputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserOutputDto;
import com.telerikacademy.web.photocontest.entities.dtos.UserUpdateDto;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;
    private final UserInputToUserConverter userInputToUserConverter;
    private final UserToUserOutputDtoConverter userToUserOutputDtoConverter;
    private final UserUpdateDtoToUserConverter userUpdateDtoToUserConverter;
    private final UserToUserIdDtoConverter userToUserIdDtoConverter;

    @GetMapping
    public List<UserOutputDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserOutputDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @PostMapping
    public ResponseEntity<UserOutputIdDto> createUser(@Valid @RequestBody UserInputDto userInputDto) {
        try {
            return ResponseEntity.ok(userService.createUser(userInputDto));
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<UserUpdateDto> editUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToEdit = userUpdateDtoToUserConverter.convert(userUpdateDto);
            userService.editUser(user, userToEdit);
            return ResponseEntity.ok(userService.editUser(user, userToEdit));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidDataAccessApiUsageException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateUser(@RequestHeader HttpHeaders headers, @Valid @PathVariable UUID id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.deactivateUser(id, user);
            return ResponseEntity.ok("User deactivated successfully!");
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InvalidDataAccessApiUsageException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided.");
        }
    }
}
