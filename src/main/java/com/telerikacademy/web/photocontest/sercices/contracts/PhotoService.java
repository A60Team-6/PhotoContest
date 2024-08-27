package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoInput;
import com.telerikacademy.web.photocontest.entities.dtos.PhotoOutput;

import java.util.List;
import java.util.UUID;

public interface PhotoService {

    Photo createPhoto(Photo photo, User user);

    List<Photo> getAll();

    Photo getByTitle(String title);

    void updatePhoto(UUID id, PhotoInput photoInputDto, User user);

    void softDeletePhoto(UUID id);

    PhotoOutput getPhotoById(UUID id);

    List<PhotoOutput> searchByTitle(String title);
}
