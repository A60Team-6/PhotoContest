package com.telerikacademy.web.photocontest.ExceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiException {

    private final HttpStatus statusCode;
    private final String message;
    private final ZonedDateTime timestamp;


}
