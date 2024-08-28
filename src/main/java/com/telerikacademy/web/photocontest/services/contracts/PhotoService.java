package com.telerikacademy.web.photocontest.services.contracts;

import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PhotoService {

    List<PhotoOutput> getAll();

    PhotoOutput getByTitle(String title);

    void updatePhoto(UUID id, PhotoUpdate photoUpdate, User user);

    PhotoOutput createPhoto(PhotoInput photoInput, User user);

    UploadFileOutput uploadPhoto(UploadFileInput uploadFileInput) throws IOException;

    void softDeletePhoto(UUID id);

    PhotoOutput getPhotoById(UUID id);

    List<PhotoOutput> searchByTitle(String title);

    Photo findPhotoEntityById(UUID id);
}