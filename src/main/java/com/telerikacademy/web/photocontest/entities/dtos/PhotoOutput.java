package com.telerikacademy.web.photocontest.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoOutput {

    private String title;
    private String story;
    private String contest;
    private String username;
    private String photo;
    private String uploadDate;
}
