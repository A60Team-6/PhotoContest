package com.telerikacademy.web.photocontest.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class UploadFileInput {
    private String photoId;
    private MultipartFile file;
}
