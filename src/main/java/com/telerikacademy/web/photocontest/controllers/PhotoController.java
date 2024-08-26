package com.telerikacademy.web.photocontest.controllers;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.AuthenticationHelper;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.PhotoInputDto;
import com.telerikacademy.web.photocontest.models.dtos.PhotoOutputDto;
import com.telerikacademy.web.photocontest.sercices.contracts.PhotoService;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final MapperHelper mapperHelper;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/{id}")
    public ResponseEntity<PhotoOutputDto> getPhotoById(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        try {
            // Проверка за авторизация, ако е необходимо
            authenticationHelper.tryGetUser(headers);

            PhotoOutputDto photoDto = photoService.getPhotoById(id);
            return ResponseEntity.ok(photoDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PhotoOutputDto>> getAll() {
        List<Photo> photos = photoService.getAll();
        List<PhotoOutputDto> photoDtos = photos.stream()
                .map(mapperHelper::changeFromPhotoToPhotoOutDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(photoDtos);
    }


    // Метод за получаване на снимка по точно заглавие
    @GetMapping("/title/{title}")
    public ResponseEntity<PhotoOutputDto> getByTitle(@PathVariable String title, @RequestHeader HttpHeaders headers) {
        try {
            authenticationHelper.tryGetUser(headers);
            Photo photo = photoService.getByTitle(title);
            return ResponseEntity.ok(mapperHelper.changeFromPhotoToPhotoOutDto(photo));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhotoOutputDto>> searchByTitle(@RequestParam String title) {
        List<PhotoOutputDto> photoDtos = photoService.searchByTitle(title);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable UUID id, @RequestHeader HttpHeaders headers) {
        try {
            // Ако има нужда от авторизация, проверете я тук
            authenticationHelper.tryGetUser(headers);

            // Извикваме метода за софт изтриване
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
                                              @RequestBody PhotoInputDto photoInputDto,
                                              @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            photoService.updatePhoto(id, photoInputDto, user);

            return new ResponseEntity<>("Photo was updated succsesfully!", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
