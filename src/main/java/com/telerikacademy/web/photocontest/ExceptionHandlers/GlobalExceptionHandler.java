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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({DuplicateEntityException.class})
//    public ResponseEntity<Object> handleDuplicateEntityException(DuplicateEntityException exception) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
//    }
//
//    @ExceptionHandler({EntityNotFoundException.class})
//    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGlobalException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
//    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {
//        HttpStatus status;
//        String message;
//
//        switch (exception.getClass().getSimpleName()) {
//            case "EntityNotFoundException":
//                status = HttpStatus.NOT_FOUND;
//                message = "The requested entity was not found.";
//                break;
//            case "DuplicateEntityException":
//                status = HttpStatus.CONFLICT;
//                message = "The entity already exists.";
//                break;
//            case "AuthorizationException":
//                status = HttpStatus.UNAUTHORIZED;
//                message = "You are not authorized to perform this action.";
//                break;
//            default:
//                status = HttpStatus.INTERNAL_SERVER_ERROR;
//                message = "An unexpected error occurred.";
//        }
//
//        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, exception.getLocalizedMessage());
//        return new ResponseEntity<>(errorResponse, status);
//    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(value = DuplicateEntityException.class)
    public ResponseEntity<ApiException> handleDuplicateEntityException(DuplicateEntityException ex, HttpServletRequest request) {
        HttpStatus badRequest = HttpStatus.CONFLICT;

        ApiException apiException = new ApiException(
                badRequest.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiException> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                badRequest.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity<ApiException> handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                badRequest.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    public ResponseEntity<ApiException> handleUnsupportedOperationException(UnsupportedOperationException ex, HttpServletRequest request) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;

        ApiException apiException = new ApiException(
                badRequest.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiException, badRequest);
    }
}
