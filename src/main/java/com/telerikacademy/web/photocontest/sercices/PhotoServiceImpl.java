package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.MapperHelper;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.PhotoInputDto;
import com.telerikacademy.web.photocontest.models.dtos.PhotoOutputDto;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final MapperHelper mapperHelper;

    @Override
    public PhotoOutputDto getPhotoById(UUID id) {
        Photo photo = photoRepository.getById(id);
//                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        return mapperHelper.changeFromPhotoToPhotoOutDto(photo);
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
    public List<PhotoOutputDto> searchByTitle(String title) {
        return photoRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(mapperHelper::changeFromPhotoToPhotoOutDto)
                .collect(Collectors.toList());
    }


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
    public void updatePhoto(UUID id, PhotoInputDto photoInputDto, User user) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        PermissionHelper.isSameUser(user,photo.getUser(),"This use is not photo owner!");

        // Актуализираме съществуващия обект Photo и го присвояваме обратно
        photo = mapperHelper.updatePhotoFromDto(photoInputDto, photo);

        // Запазваме актуализирания обект в базата данни
        Photo updatedPhoto = photoRepository.save(photo);

    }


}
