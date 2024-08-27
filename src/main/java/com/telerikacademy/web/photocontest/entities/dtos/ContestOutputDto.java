package com.telerikacademy.web.photocontest.entities.dtos;

import com.telerikacademy.web.photocontest.entities.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestOutputDto {

    private String title;
    private String category;
    private Phase phase;

}
