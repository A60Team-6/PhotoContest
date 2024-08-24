package com.telerikacademy.web.photocontest.sercices.contracts;

import com.telerikacademy.web.photocontest.models.Photo;
import com.telerikacademy.web.photocontest.models.User;
import com.telerikacademy.web.photocontest.models.dtos.PhotoInputDto;

public interface PhotoService {

    Photo createPhoto(Photo photo, User user);
}
