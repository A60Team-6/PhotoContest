package com.telerikacademy.web.photocontest.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.web.photocontest.services.contracts.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        System.out.println("Uploading file: " + file.getOriginalFilename());
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            System.out.println("Upload result: " + uploadResult);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            System.err.println("Failed to upload file: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to upload photo", e);
        }
    }
}
