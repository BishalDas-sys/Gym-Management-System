package com.gymmanagement.gym_management_application;

import com.gymmanagement.gym_management_application.dto.ClassDto;
import com.gymmanagement.gym_management_application.dto.ClassResponseDto;
import com.gymmanagement.gym_management_application.entity.ClubClass;
import com.gymmanagement.gym_management_application.exception.ValidationException;
import com.gymmanagement.gym_management_application.repository.ClubClassRepository;
import com.gymmanagement.gym_management_application.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
    void testCreateClass_OverlappingClass() {
        // Prepare test data for the new class to be created
        ClassDto classDto = new ClassDto(
                "Yoga Class",
                LocalDate.now().plusDays(2),  // Start date
                LocalDate.now().plusDays(4),  // End date
                LocalTime.of(10, 0),  // Start time
                60,  // Duration
                10   // Capacity
        );

        // Mock the behavior to simulate an existing class in the overlapping date range
        ClubClass existingClass = new ClubClass();
        existingClass.setId(1L);
        existingClass.setName("Existing Yoga Class");
        existingClass.setStartDate(LocalDate.now().plusDays(1));  // Overlaps with the new class
        existingClass.setEndDate(LocalDate.now().plusDays(3));    // Overlaps with the new class
        existingClass.setStartTime(LocalTime.of(9, 0));
        existingClass.setDuration(60);
        existingClass.setCapacity(20);

        // Mock repository method to return a conflicting class
        when(clubClassRepository.findFirstByStartDateBeforeAndEndDateAfter(
                classDto.getEndDate(), classDto.getStartDate()))
                .thenReturn(existingClass);

        // Verify that a ValidationException is thrown due to the overlapping class
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            classService.createClass(classDto);
        });

        // Verify that the exception message contains the correct information about the conflicting class
        assertTrue(thrown.getMessage().contains("A class already exists in this date range."));
        assertTrue(thrown.getMessage().contains("Start Date - " + existingClass.getStartDate()));
        assertTrue(thrown.getMessage().contains("End Date - " + existingClass.getEndDate()));
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

    @Test
    void testGetAllClasses() {
        // Prepare test data: a list of ClubClass entities
        ClubClass class1 = new ClubClass();
        class1.setId(1L);
        class1.setName("Yoga Class 1");
        class1.setStartDate(LocalDate.now().plusDays(1));
        class1.setEndDate(LocalDate.now().plusMonths(1));
        class1.setStartTime(LocalTime.of(9, 0));
        class1.setDuration(60);
        class1.setCapacity(20);

        ClubClass class2 = new ClubClass();
        class2.setId(2L);
        class2.setName("Yoga Class 2");
        class2.setStartDate(LocalDate.now().plusDays(2));
        class2.setEndDate(LocalDate.now().plusMonths(2));
        class2.setStartTime(LocalTime.of(11, 0));
        class2.setDuration(60);
        class2.setCapacity(25);

        List<ClubClass> allClasses = List.of(class1, class2);

        // Mock the repository method to return the list of classes
        when(clubClassRepository.findAll()).thenReturn(allClasses);

        // Execute the method
        List<ClassResponseDto> responseDtos = classService.getAllClasses();

        // Verify the results
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());

        // Verify that each class is mapped correctly
        ClassResponseDto responseDto1 = responseDtos.get(0);
        assertEquals(class1.getId(), responseDto1.getClassId());
        assertEquals(class1.getName(), responseDto1.getName());
        assertEquals(class1.getStartDate(), responseDto1.getStartDate());
        assertEquals(class1.getEndDate(), responseDto1.getEndDate());
        assertEquals(class1.getStartTime(), responseDto1.getStartTime());
        assertEquals(class1.getDuration(), responseDto1.getDuration());
        assertEquals(class1.getCapacity(), responseDto1.getCapacity());

        ClassResponseDto responseDto2 = responseDtos.get(1);
        assertEquals(class2.getId(), responseDto2.getClassId());
        assertEquals(class2.getName(), responseDto2.getName());
        assertEquals(class2.getStartDate(), responseDto2.getStartDate());
        assertEquals(class2.getEndDate(), responseDto2.getEndDate());
        assertEquals(class2.getStartTime(), responseDto2.getStartTime());
        assertEquals(class2.getDuration(), responseDto2.getDuration());
        assertEquals(class2.getCapacity(), responseDto2.getCapacity());
    }

    @Test
    void testCreateClass_OverlapWithExistingClass() {
        // Prepare test data: a class that overlaps with an existing one
        ClassDto classDto = new ClassDto(
                "New Yoga Class",
                LocalDate.now().plusDays(2),  // Start date
                LocalDate.now().plusDays(5),  // End date
                LocalTime.of(10, 0),         // Start time
                60,                          // Duration
                10                           // Capacity
        );

        // Prepare an existing conflicting class
        ClubClass existingClass = new ClubClass();
        existingClass.setId(1L);
        existingClass.setName("Existing Yoga Class");
        existingClass.setStartDate(LocalDate.now().plusDays(1));  // Overlapping start date
        existingClass.setEndDate(LocalDate.now().plusDays(4));    // Overlapping end date
        existingClass.setStartTime(LocalTime.of(9, 0));           // Different start time
        existingClass.setDuration(60);                             // Same duration
        existingClass.setCapacity(15);                             // Different capacity

        // Mock the repository to return the existing conflicting class
        when(clubClassRepository.findFirstByStartDateBeforeAndEndDateAfter(
                classDto.getEndDate(), classDto.getStartDate()))
                .thenReturn(existingClass);

        // Verify that ValidationException is thrown due to overlapping classes
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            classService.createClass(classDto);
        });

        // Verify that the exception message contains the expected error
        assertEquals("A class already exists in this date range. Conflicting class: Start Date - " +
                        existingClass.getStartDate() + ", End Date - " + existingClass.getEndDate(),
                exception.getMessage());
    }


}