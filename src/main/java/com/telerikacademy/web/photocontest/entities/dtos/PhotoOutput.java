package com.telerikacademy.web.photocontest.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoOutput {

    private String title;
    private String story;
    private String photoUrl;
    private String contest;
    private String user;
    private LocalDateTime createdAt;

}
