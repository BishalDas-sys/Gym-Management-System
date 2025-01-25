package com.gymmanagement.gym_management_application.exception;

import com.gymmanagement.gym_management_application.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse> handleValidationException(ValidationException ex) {
        GenericResponse response = new GenericResponse();
        response.failure(ex.getMessage(), "VALIDATION_ERROR");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<GenericResponse> handleCapacityExceededException(CapacityExceededException ex) {
        GenericResponse response = new GenericResponse();
        response.failure(ex.getMessage(), "CAPACITY_EXCEEDED");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle NoRecordsFoundException globally
    @ExceptionHandler(NoRecordsFoundException.class)
    public ResponseEntity<GenericResponse> handleNoRecordsFoundException(NoRecordsFoundException e) {
        // Return a response with a NOT_FOUND status and the exception message
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new GenericResponse().failure(e.getMessage(), "NO_RECORDS_FOUND")
        );
    }

    // Handle ClassNotFoundException
    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<GenericResponse> handleClassNotFound(ClassNotFoundException ex) {
        GenericResponse response = new GenericResponse()
                .failure(ex.getMessage(), "CLASS_NOT_FOUND");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidParticipationDateException.class)
    public ResponseEntity<GenericResponse> handleInvalidParticipationDateException(InvalidParticipationDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GenericResponse().failure(e.getMessage(), "INVALID_DATE"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleGenericException(Exception ex) {
        GenericResponse response = new GenericResponse();
        response.failure("An unexpected error occurred. Please try again.", "INTERNAL_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
