package com.telerikacademy.web.photocontest.ExceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiException {

    private final int statusCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final String path;
}
