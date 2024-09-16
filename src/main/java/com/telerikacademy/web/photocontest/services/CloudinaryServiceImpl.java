package com.telerikacademy.web.photocontest.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.web.photocontest.services.contracts.CloudinaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        log.info("Uploading file: {}", file.getOriginalFilename());
        try {
            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            log.debug("Upload result: {}", uploadResult);

            return Optional.ofNullable((String) uploadResult.get("url"))
                    .orElseThrow(() -> new FileUploadException("URL not found in upload result"));
        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage(), e);
            throw new FileUploadException("Failed to upload photo", e);
        }
    }
}

