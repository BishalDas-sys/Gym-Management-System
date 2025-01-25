package com.gymmanagement.gym_management_application.dto;

import com.gymmanagement.gym_management_application.entity.ClubClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {

    private Long bookingId;
    private String memberName;
    private ClubClass gymClass;
    private LocalDate participationDate;
    private LocalTime classStartTime;

    public BookingResponseDto(String bookingCreatedSuccessfully) {
    }
}
