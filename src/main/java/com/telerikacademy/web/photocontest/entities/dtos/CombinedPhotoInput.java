package com.telerikacademy.web.photocontest.entities.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CombinedPhotoInput {

    @NotNull
    private PhotoInput photoInput;
    private MultipartFile file;
}
