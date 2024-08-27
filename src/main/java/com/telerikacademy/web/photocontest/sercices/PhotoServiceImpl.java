package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoInput;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoOutput;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final MapperHelper mapperHelper;
    private final Converter<Photo, PhotoOutput> photoToPhotoOutputConverter;
    private final Converter<PhotoInput, Photo> photoInputToPhotoConverter;

    @Override
    public PhotoOutput getPhotoById(UUID id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        return photoToPhotoOutputConverter.convert(photo);
    }

    @Override
    public List<Photo> getAll() {
        return photoRepository.findAllByIsActiveTrue();
    }

    @Override
    public Photo getByTitle(String title) {
        return photoRepository.findByTitleAndIsActiveTrue(title);
    }

    @Override
    public List<PhotoOutput> searchByTitle(String title) {
        return photoRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(mapperHelper::changeFromPhotoToPhotoOutDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public PhotoOutput createPhoto(PhotoInput photoInput, User user) {
//        Photo photo = photoInputToPhotoConverter.convert(photoInput);
//        photo.setUser(user);
//        user.getPhotos().add(photo);
//        photo = photoRepository.save(photo);
//        return photoToPhotoOutputConverter.convert(photo);
//    }

    @Override
    public Photo createPhoto(Photo photo, User user) {
        photo.setUser(user);
        user.getPhotos().add(photo);

        Photo exsistingPhoto = photoRepository.findByPhotoUrl(photo.getPhotoUrl());
        if (exsistingPhoto != null) {
            throw new DuplicateEntityException("Photo", "photo url", photo.getPhotoUrl());
        }

        return photoRepository.save(photo);
    }

    @Override
    public void softDeletePhoto(UUID id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

        // Променяме isActive на false
        photo.setIsActive(false);

        // Запазваме актуализирания обект обратно в базата данни
        photoRepository.save(photo);
    }

    @Override
    public void updatePhoto(UUID id, PhotoInput photoInputDto, User user) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        PermissionHelper.isSameUser(user, photo.getUser(), "This use is not photo owner!");

        // Актуализираме съществуващия обект Photo и го присвояваме обратно
        photo = mapperHelper.updatePhotoFromDto(photoInputDto, photo);

        // Запазваме актуализирания обект в базата данни
        Photo updatedPhoto = photoRepository.save(photo);

    }


}
