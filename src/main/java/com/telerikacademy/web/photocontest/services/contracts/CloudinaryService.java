package com.telerikacademy.web.photocontest.services.contracts;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadFile(MultipartFile file) throws IOException;
}
