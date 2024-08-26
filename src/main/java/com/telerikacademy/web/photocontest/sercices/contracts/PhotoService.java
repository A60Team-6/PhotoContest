package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.PhotoInputDto;
import com.telerikacademy.web.photocontest.models.dtos.PhotoOutputDto;

import java.util.List;
import java.util.UUID;

public interface PhotoService {

    Photo createPhoto(Photo photo, User user);

    List<Photo> getAll();

    Photo getByTitle(String title);

    void updatePhoto(UUID id, PhotoInputDto photoInputDto, User user);

    void softDeletePhoto(UUID id);

    PhotoOutputDto getPhotoById(UUID id);

    List<PhotoOutputDto> searchByTitle(String title);
}
