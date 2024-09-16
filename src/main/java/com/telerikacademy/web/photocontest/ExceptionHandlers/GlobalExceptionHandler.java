package com.telerikacademy.web.photocontest.ExceptionHandlers;

import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = DuplicateEntityException.class)
    public ResponseEntity<ApiException> handleDuplicateEntityException(DuplicateEntityException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                httpStatus.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiException> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                httpStatus.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<ApiException> handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                httpStatus.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    public ResponseEntity<ApiException> handleUnsupportedOperationException(UnsupportedOperationException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                httpStatus.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiException> handleException(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiException apiException = new ApiException(
                httpStatus.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "File size exceeds the limit!");
        return "redirect:/uploadError";
    }
}
