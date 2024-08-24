package com.telerikacademy.web.photocontest.sercices;

import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.sercices.contracts.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

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
}
