package com.telerikacademy.web.photocontest.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoOutputDto {

    private String title;
    private String story;
    private String photoUrl;
    private String contest;
    private String user;
    private LocalDateTime createdAt;

}
