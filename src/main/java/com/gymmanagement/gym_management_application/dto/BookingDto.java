package com.gymmanagement.gym_management_application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    @NotNull(message = "Member name is required.")
    @Size(min = 3, max = 100, message = "Member name must be between 3 and 100 characters.")
    private String memberName;

    @NotNull(message = "Class Id is required.")
    private Long classId;

    @NotNull(message = "Participation date is required.")
    @Future(message = "Participation date must be in the future.")
    private LocalDate participationDate;

    private LocalTime classStartTime;

    public BookingDto(String janeDoe, long l, LocalDate localDate) {
    }
}
