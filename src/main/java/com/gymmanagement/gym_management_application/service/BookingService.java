package com.gymmanagement.gym_management_application.service;

import com.gymmanagement.gym_management_application.dto.BookingDto;
import com.gymmanagement.gym_management_application.dto.BookingResponseDto;
import com.gymmanagement.gym_management_application.entity.Booking;
import com.gymmanagement.gym_management_application.entity.ClubClass;
import com.gymmanagement.gym_management_application.exception.NoRecordsFoundException;
import com.gymmanagement.gym_management_application.repository.BookingRepository;
import com.gymmanagement.gym_management_application.repository.ClubClassRepository;
import com.gymmanagement.gym_management_application.exception.ClassNotFoundException;
import com.gymmanagement.gym_management_application.exception.InvalidParticipationDateException;
import com.gymmanagement.gym_management_application.exception.CapacityExceededException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClubClassRepository classRepository;

    public BookingService(BookingRepository bookingRepository, ClubClassRepository classRepository) {
        this.bookingRepository = bookingRepository;
        this.classRepository = classRepository;
    }

    // Method to create a booking using BookingDto in service layer
    public BookingResponseDto createBooking(BookingDto bookingDto) {
        // Fetch the class by ID
        ClubClass clazz = classRepository.findById(bookingDto.getClassId())
                .orElseThrow(() -> new ClassNotFoundException("Class not found."));

        // Ensure the participation date is in the future
        if (bookingDto.getParticipationDate().isBefore(LocalDate.now())) {
            throw new InvalidParticipationDateException("Participation date must be in the future.");
        }


        // Count the number of bookings for the class on the given participation date
        long bookingsCount = bookingRepository.findByParticipationDateBetween(bookingDto.getParticipationDate(), bookingDto.getParticipationDate()).stream()
                .filter(booking -> booking.getClazz().getId().equals(bookingDto.getClassId()))
                .count();

        // Check if the class has exceeded capacity
        if (bookingsCount >= clazz.getCapacity()) {
            throw new CapacityExceededException("Class is already at full capacity. Can't book " +
                    "this class");
        }

        // Create a new Booking entity
        Booking booking = new Booking();
        booking.setMemberName(bookingDto.getMemberName());
        booking.setClazz(clazz);
        booking.setParticipationDate(bookingDto.getParticipationDate());

        // Save the booking in the repository
        Booking savedBooking = bookingRepository.save(booking);

        // Map the saved entity back to BookingDto
        BookingResponseDto savedBookingDto = new BookingResponseDto();
        savedBookingDto.setBookingId(savedBooking.getId());
        savedBookingDto.setMemberName(savedBooking.getMemberName());
        savedBookingDto.setGymClass(savedBooking.getClazz());
        savedBookingDto.setParticipationDate(savedBooking.getParticipationDate());
        savedBookingDto.setClassStartTime(savedBooking.getClazz().getStartTime());

        return savedBookingDto;
    }

    // Method to search bookings using BookingDto in service layer
    public List<BookingResponseDto> searchBookings(String memberName, LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings;

        // Fetch the bookings based on search criteria
        if (memberName != null && startDate != null && endDate != null) {
            bookings = bookingRepository.findByMemberNameAndParticipationDateBetween(memberName, startDate, endDate);
        } else if (memberName != null) {
            bookings = bookingRepository.findByMemberName(memberName);
        } else if (startDate != null && endDate != null) {
            bookings = bookingRepository.findByParticipationDateBetween(startDate, endDate);
        } else {
            bookings = bookingRepository.findAll();
        }

        // If no records are found, return a custom message
        if (bookings.isEmpty()) {
            throw new NoRecordsFoundException("No records found for the provided search criteria.");
        }

        // Map the list of Booking entities to BookingDto
        return bookings.stream().map(booking -> {
            BookingResponseDto dto = new BookingResponseDto();
            dto.setBookingId(booking.getId());
            dto.setMemberName(booking.getMemberName());
            dto.setGymClass(booking.getClazz());
            dto.setParticipationDate(booking.getParticipationDate());
            dto.setClassStartTime(booking.getClazz().getStartTime());
            return dto;
        }).collect(Collectors.toList());
    }

}
