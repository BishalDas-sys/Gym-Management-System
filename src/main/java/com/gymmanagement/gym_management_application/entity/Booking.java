package com.gymmanagement.gym_management_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String memberName;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClubClass clazz;

    @NotNull
    private LocalDate participationDate;
}
