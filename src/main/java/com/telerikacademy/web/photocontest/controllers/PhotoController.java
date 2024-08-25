package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.models.Contest;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.PhotoInputDto;
import com.telerikacademy.web.photocontest.models.dtos.PhotoOutputDto;
import com.telerikacademy.web.photocontest.sercices.contracts.PhotoService;
import com.telerikacademy.web.photocontest.sercices.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;


    @GetMapping
    public ResponseEntity<List<PhotoOutputDto>> getAll() {
        List<Photo> photos = photoService.getAll();
        List<PhotoOutputDto> photoDtos = photos.stream()
                .map(mapperHelper::changeFromPhotoToPhotoOutDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(photoDtos);
    }

    @PostMapping
    public ResponseEntity<Photo> createPhoto(@Valid @RequestBody PhotoInputDto photoInputDto, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Photo photo = mapperHelper.createPhotoFromPhotoInputDto(photoInputDto);
            return ResponseEntity.ok(photoService.createPhoto(photo, user));
        } catch (EntityNotFoundException e) {
            // Ако потребителят не е намерен
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            // Ако има дублиране на потребителско име или имейл
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
