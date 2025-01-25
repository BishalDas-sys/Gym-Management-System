package com.gymmanagement.gym_management_application;

import com.gymmanagement.gym_management_application.dto.ClassDto;
import com.gymmanagement.gym_management_application.dto.ClassResponseDto;
import com.gymmanagement.gym_management_application.entity.ClubClass;
import com.gymmanagement.gym_management_application.exception.CapacityExceededException;
import com.gymmanagement.gym_management_application.exception.ValidationException;
import com.gymmanagement.gym_management_application.repository.ClubClassRepository;
import com.gymmanagement.gym_management_application.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClubClassUnitTest {

    private ClassService classService;

    @Mock
    private ClubClassRepository clubClassRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classService = new ClassService(clubClassRepository);
    }

    @Test
    void testCreateClass_Success() {
        // Prepare test data
        ClassDto classDto = new ClassDto(
                "Yoga Class",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1),
                LocalTime.of(10, 0),
                60,
                5
        );

        // Create a mock saved class with an ID
        ClubClass savedClass = new ClubClass();
        savedClass.setId(1L);
        savedClass.setName(classDto.getName());
        savedClass.setStartDate(classDto.getStartDate());
        savedClass.setEndDate(classDto.getEndDate());
        savedClass.setStartTime(classDto.getStartTime());
        savedClass.setDuration(classDto.getDuration());
        savedClass.setCapacity(classDto.getCapacity());

        // Mock repository save behavior
        when(clubClassRepository.save(any(ClubClass.class))).thenReturn(savedClass);

        // Execute the method
        ClassResponseDto responseDto = classService.createClass(classDto);

        // Verify the results
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getClassId());
        assertEquals(classDto.getName(), responseDto.getName());
    }

    @Test
    void testCreateClass_CapacityExceeded() {
        // Prepare test data with capacity exceeding limit
        ClassDto classDto = new ClassDto(
                "Yoga Class",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1),
                LocalTime.of(10, 0),
                60,
                11  // Exceeds max capacity of 10
        );

        // Verify that CapacityExceededException is thrown
        assertThrows(CapacityExceededException.class, () -> {
            classService.createClass(classDto);
        });
    }

    @Test
    void testCreateClass_InvalidEndDate() {
        // Prepare test data with end date in the past
        ClassDto classDto = new ClassDto(
                "Yoga Class",
                LocalDate.now().minusMonths(2),
                LocalDate.now().minusMonths(1),
                LocalTime.of(10, 0),
                60,
                5
        );

        // Verify that ValidationException is thrown
        assertThrows(ValidationException.class, () -> {
            classService.createClass(classDto);
        });
    }

    @Test
    void testCreateClass_EndDateBeforeStartDate() {
        // Prepare test data with end date before start date
        ClassDto classDto = new ClassDto(
                "Yoga Class",
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusDays(10),
                LocalTime.of(10, 0),
                60,
                5
        );

        // Verify that ValidationException is thrown
        assertThrows(ValidationException.class, () -> {
            classService.createClass(classDto);
        });
    }

    @Test
    void testCreateClass_InvalidCapacity() {
        // Prepare test data with zero capacity
        ClassDto classDto = new ClassDto(
                "Yoga Class",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1),
                LocalTime.of(10, 0),
                60,
                0
        );

        // Verify that ValidationException is thrown
        assertThrows(ValidationException.class, () -> {
            classService.createClass(classDto);
        });
    }
}