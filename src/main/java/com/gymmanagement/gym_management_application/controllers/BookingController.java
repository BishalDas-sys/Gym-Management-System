package com.gymmanagement.gym_management_application.controllers;

import com.gymmanagement.gym_management_application.dto.BookingDto;
import com.gymmanagement.gym_management_application.dto.BookingResponseDto;
import com.gymmanagement.gym_management_application.dto.GenericResponse;
import com.gymmanagement.gym_management_application.exception.ClassNotFoundException;
import com.gymmanagement.gym_management_application.exception.InvalidParticipationDateException;
import com.gymmanagement.gym_management_application.exception.CapacityExceededException;
import com.gymmanagement.gym_management_application.exception.NoRecordsFoundException;
import com.gymmanagement.gym_management_application.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST request to create a new booking
    @PostMapping
    public ResponseEntity<GenericResponse> createBooking(@RequestBody @Valid BookingDto bookingDto, BindingResult bindingResult) {

        // Check if validation fails for BookingDto
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(". ", "Please provide proper payload. ", "."));

            return ResponseEntity.badRequest().body(
                    new GenericResponse().failure(errorMessage.trim(), "VALIDATION_ERROR")
            );
        }

        try {
            // Call the BookingService to create the booking with the provided details
            BookingResponseDto createdBooking = bookingService.createBooking(bookingDto);

            // Return a successful response with a message and the created booking details
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new GenericResponse().success("Booking created successfully.", createdBooking)
            );
        } catch (ClassNotFoundException e) {
            log.error("Class not found: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new GenericResponse().failure(e.getMessage(), "CLASS_NOT_FOUND")
            );
        } catch (InvalidParticipationDateException e) {
            log.error("Invalid participation date: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse().failure(e.getMessage(), "INVALID_DATE")
            );
        } catch (CapacityExceededException e) {
            log.error("Class capacity exceeded: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse().failure(e.getMessage(), "CAPACITY_EXCEEDED")
            );
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new GenericResponse().failure("An unexpected error occurred.", "INTERNAL_ERROR")
            );
        }
    }


    // GET request to search for bookings
    @GetMapping
    public ResponseEntity<GenericResponse> searchBookings(@RequestParam(required = false) String memberName,
                                                          @RequestParam(required = false) LocalDate startDate,
                                                          @RequestParam(required = false) LocalDate endDate) {
        try {
            // Call the BookingService to search bookings based on the provided criteria
            List<BookingResponseDto> bookings = bookingService.searchBookings(memberName, startDate, endDate);

            // Return a successful response with the list of bookings
            return ResponseEntity.ok(new GenericResponse().success("Bookings fetched successfully.", bookings));

        } catch (NoRecordsFoundException e) {
            // Handle NoRecordsFoundException if no records are found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new GenericResponse().failure(e.getMessage(), "NO_RECORDS_FOUND")
            );
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException if the class is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new GenericResponse().failure(e.getMessage(), "CLASS_NOT_FOUND")
            );
        } catch (InvalidParticipationDateException e) {
            // Handle InvalidParticipationDateException if the participation date is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse().failure(e.getMessage(), "INVALID_DATE")
            );
        } catch (CapacityExceededException e) {
            // Handle CapacityExceededException if the class exceeds the maximum capacity
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new GenericResponse().failure(e.getMessage(), "CAPACITY_EXCEEDED")
            );
        } catch (Exception e) {
            // Handle any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new GenericResponse().failure("An unexpected error occurred.", "INTERNAL_ERROR")
            );
        }
    }


}
