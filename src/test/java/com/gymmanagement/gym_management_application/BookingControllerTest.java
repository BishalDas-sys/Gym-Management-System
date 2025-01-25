package com.gymmanagement.gym_management_application;

import com.gymmanagement.gym_management_application.controllers.BookingController;
import com.gymmanagement.gym_management_application.dto.BookingDto;
import com.gymmanagement.gym_management_application.dto.BookingResponseDto;
import com.gymmanagement.gym_management_application.dto.GenericResponse;
import com.gymmanagement.gym_management_application.exception.CapacityExceededException;
import com.gymmanagement.gym_management_application.exception.ClassNotFoundException;
import com.gymmanagement.gym_management_application.exception.NoRecordsFoundException;
import com.gymmanagement.gym_management_application.repository.ClubClassRepository;
import com.gymmanagement.gym_management_application.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ClubClassRepository classRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking_Success() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setMemberName("John Doe");
        bookingDto.setClassId(1L);
        bookingDto.setParticipationDate(LocalDate.now().plusDays(1)); // Date in the future

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBookingId(1L);

        when(bookingService.createBooking(bookingDto)).thenReturn(bookingResponseDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<GenericResponse> response = bookingController.createBooking(bookingDto, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData());
        assertEquals("Booking created successfully.", response.getBody().getMessage());
    }

    @Test
    void testCreateBooking_ValidationError() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("bookingDto", "memberName", "Member Name is required")));

        BookingDto bookingDto = new BookingDto();
        ResponseEntity<GenericResponse> response = bookingController.createBooking(bookingDto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Please provide proper payload"));
    }

    @Test
    void testSearchBookings_Success() {
        List<BookingResponseDto> bookings = List.of(new BookingResponseDto());
        when(bookingService.searchBookings("John Doe", null, null)).thenReturn(bookings);

        ResponseEntity<GenericResponse> response = bookingController.searchBookings("John Doe", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, ((List) response.getBody().getData()).size());
        assertEquals("Bookings fetched successfully.", response.getBody().getMessage());
    }

    @Test
    void testSearchBookings_NoRecordsFound() {
        when(bookingService.searchBookings("John Doe", null, null)).thenThrow(new NoRecordsFoundException("No records found"));

        ResponseEntity<GenericResponse> response = bookingController.searchBookings("John Doe", null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No records found", response.getBody().getMessage());
    }
}
