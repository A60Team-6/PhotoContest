package com.telerikacademy.web.photocontest.controllers;

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

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> getUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(mapperHelper.changeFromUserToUserOutputDto(user));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserOutputDto> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return ResponseEntity.ok(mapperHelper.changeFromUserToUserOutputDto(user));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserInputDto userInputDto) {
        try {
            User user = mapperHelper.createUserFromUserInputDto(userInputDto);
            userService.createUser(user);
            return ResponseEntity.ok(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToEdit = mapperHelper.updateUserFromUserInputDto(userUpdateDto);
            userService.editUser(user, userToEdit);
            return ResponseEntity.ok("User edited successfully!");
        } catch (EntityNotFoundException e) {
            // Ако потребителят не е намерен
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DuplicateEntityException e) {
            // Ако има дублиране на потребителско име или имейл
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (InvalidDataAccessApiUsageException e) {
            // Ако се направи неправилен достъп до базата данни
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@RequestHeader HttpHeaders headers, @Valid @PathVariable UUID id) {
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
