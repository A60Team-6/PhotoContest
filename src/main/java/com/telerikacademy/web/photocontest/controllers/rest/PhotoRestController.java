package com.telerikacademy.web.photocontest.controllers.rest;

import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        authenticationHelper.tryGetUser(headers);

        PhotoOutput photoDto = photoService.getPhotoById(id);
        return ResponseEntity.ok(photoDto);
    }

    @GetMapping
    public ResponseEntity<List<PhotoOutput>> getAll() {

        return ResponseEntity.ok(photoService.getAll());
    }

    @GetMapping("/title")
    public ResponseEntity<PhotoOutput> getByTitle(@RequestParam String title, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);
        return ResponseEntity.ok(photoService.getByTitle(title));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhotoOutput>> searchByTitle(@RequestParam String title) {
        List<PhotoOutput> photoDtos = photoService.searchByTitle(title);
        return ResponseEntity.ok(photoDtos);
    }

    @PostMapping
    public ResponseEntity<PhotoIdOutput> createPhoto(@RequestBody PhotoInput photoInput,
                                                     @RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        PhotoIdOutput createdPhoto = photoService.createPhoto(photoInput, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
    }

    @PostMapping("/upload/{id}")
    @SneakyThrows
    public ResponseEntity<String> uploadFile(@RequestPart(name = "file") MultipartFile file,
                                             @PathVariable String id) {
        UploadFileInput uploadFileInput = new UploadFileInput(id, file);

        photoService.uploadPhoto(uploadFileInput);

        return ResponseEntity.ok("Photo was uploaded successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetUser(headers);

        photoService.softDeletePhoto(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePhoto(@PathVariable UUID id,
                                              @RequestBody PhotoUpdate photoUpdate,
                                              @RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        photoService.updatePhoto(id, photoUpdate, user);

        return new ResponseEntity<>("Photo was updated succsesfully!", HttpStatus.OK);
    }

}
