package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoRestController {

    private final PhotoService photoService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/{id}")
    public ResponseEntity<PhotoOutput> getPhotoById(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);

            PhotoOutput photoDto = photoService.getPhotoById(id);
            return ResponseEntity.ok(photoDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PhotoOutput>> getAll() {

        return ResponseEntity.ok(photoService.getAll());
    }

    @GetMapping("/title")
    public ResponseEntity<PhotoOutput> getByTitle(@RequestParam String title, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            return ResponseEntity.ok(photoService.getByTitle(title));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhotoOutput>> searchByTitle(@RequestParam String title) {
        List<PhotoOutput> photoDtos = photoService.searchByTitle(title);
        return ResponseEntity.ok(photoDtos);
    }

    @PostMapping
    public ResponseEntity<PhotoIdOutput> createPhoto(@RequestBody PhotoInput photoInput,
                                                     @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            PhotoIdOutput createdPhoto = photoService.createPhoto(photoInput, user);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(@RequestPart(name = "file") MultipartFile file, @PathVariable String id){
        try {
            UploadFileInput uploadFileInput = new UploadFileInput(id, file);

            photoService.uploadPhoto(uploadFileInput);

            return ResponseEntity.ok("Photo was uploaded successfully." );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Photo not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);

            photoService.softDeletePhoto(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePhoto(@PathVariable UUID id,
                                              @RequestBody PhotoUpdate photoUpdate,
                                              @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            photoService.updatePhoto(id, photoUpdate, user);

            return new ResponseEntity<>("Photo was updated succsesfully!", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
