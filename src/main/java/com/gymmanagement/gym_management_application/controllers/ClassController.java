package com.gymmanagement.gym_management_application.controllers;

import com.gymmanagement.gym_management_application.dto.ClassDto;
import com.gymmanagement.gym_management_application.dto.ClassResponseDto;
import com.gymmanagement.gym_management_application.dto.GenericResponse;
import com.gymmanagement.gym_management_application.exception.CapacityExceededException;
import com.gymmanagement.gym_management_application.exception.ValidationException;
import com.gymmanagement.gym_management_application.service.ClassService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    // Endpoint for creating a new class
    @PostMapping
    public ResponseEntity<GenericResponse> createClass(@RequestBody @Valid ClassDto classDto, BindingResult bindingResult) {
        // Check if validation fails for ClassDto
        if (bindingResult.hasErrors()) {
            // Return a 400 Bad Request if validation errors are found
            String errorMessage = "Please provide proper payload. ";
            for (var error : bindingResult.getFieldErrors()) {
                errorMessage += error.getField() + ": " + error.getDefaultMessage() + ". ";
            }
            return ResponseEntity.badRequest().body(
                    new GenericResponse().failure(errorMessage.trim(), "VALIDATION_ERROR")
            );
        }

        try {
            // Call the ClassService to create the class with the provided details
            ClassResponseDto createdClass = classService.createClass(classDto);

            // Return a successful response with a message and the created class details
            return ResponseEntity.ok(new GenericResponse().success("Class created successfully.", createdClass));
        } catch (ValidationException e) {
            // Catch ValidationException for validation errors like invalid dates or capacity
            return ResponseEntity.badRequest().body(
                    new GenericResponse().failure(e.getMessage(), "VALIDATION_ERROR")
            );
        } catch (CapacityExceededException e) {
            // Catch CapacityExceededException if the class exceeds the maximum capacity
            return ResponseEntity.badRequest().body(
                    new GenericResponse().failure(e.getMessage(), "CAPACITY_EXCEEDED")
            );
        } catch (Exception e) {
            // Catch any other exceptions and return a generic error response
            return ResponseEntity.status(500).body(
                    new GenericResponse().failure("An unexpected error occurred.", "INTERNAL_SERVER_ERROR")
            );
        }
    }
}
