package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import com.telerikacademy.web.photocontest.services.contracts.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final PhotoService photoService;

    @GetMapping
    public List<UserOutput> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutput> getUserById(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        User authUser = authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(userService.findUserById(id, authUser));
    }

    @GetMapping("/username")
    public ResponseEntity<UserOutput> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @GetMapping("/user/photos")
    public ResponseEntity<List<PhotoOutput>> getAllPhotosOfUser(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(photoService.getAllPhotosOfUser(authenticationHelper.tryGetUser(headers)));
    }

    @PostMapping
    public ResponseEntity<UserOutputId> createUser(@Valid @RequestBody Register register) {
        return ResponseEntity.ok(userService.createUser(register));
    }

    @PostMapping("/upload/{id}")
    @SneakyThrows
    public ResponseEntity<String> uploadFile(@RequestPart(name = "file") MultipartFile file,
                                             @PathVariable String id) {

        userService.uploadPhoto(id, file);

        return ResponseEntity.ok("Photo was uploaded successfully.");
    }

    @PutMapping
    public ResponseEntity<UserUpdate> editUser(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserUpdate userUpdate) {
        User user = authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(userService.editUser(user, userUpdate));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateUser(@RequestHeader HttpHeaders headers, @Valid @PathVariable UUID id) {
        User user = authenticationHelper.tryGetUser(headers);
        userService.deactivateUser(id, user);
        return ResponseEntity.ok("User deactivated successfully!");
    }

//    празен ред
}
