package com.telerikacademy.web.photocontest.ExceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiException {

    private final int statusCode;
    private final String message;
    private final LocalDateTime timestamp;
    private final String path;


}
