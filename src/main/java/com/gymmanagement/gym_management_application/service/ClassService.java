package com.gymmanagement.gym_management_application.service;

import com.gymmanagement.gym_management_application.dto.ClassDto;
import com.gymmanagement.gym_management_application.dto.ClassResponseDto;
import com.gymmanagement.gym_management_application.entity.ClubClass;
import com.gymmanagement.gym_management_application.exception.CapacityExceededException;
import com.gymmanagement.gym_management_application.exception.ValidationException;
import com.gymmanagement.gym_management_application.repository.ClubClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    private final ClubClassRepository clubClassRepository;

    public ClassService(ClubClassRepository clubClassRepository) {
        this.clubClassRepository = clubClassRepository;
    }

    // Method to create a new class by validating input details and saving the class
    public ClassResponseDto createClass(ClassDto classDto) {
        // Validate the class details (e.g., end date, capacity)
        validateClassDetails(classDto);

        // Map DTO to entity object for saving to the database
        ClubClass clazz = new ClubClass();
        clazz.setName(classDto.getName());
        clazz.setStartDate(classDto.getStartDate());
        clazz.setEndDate(classDto.getEndDate());
        clazz.setStartTime(classDto.getStartTime());
        clazz.setDuration(classDto.getDuration());
        clazz.setCapacity(classDto.getCapacity());

        // Save the class Details to the database
        ClubClass savedClass = clubClassRepository.save(clazz);

        // Map the saved class Details back to DTO for return
        return mapToDto(savedClass);
    }

    // Private method for validating class details
    private void validateClassDetails(ClassDto classDto) {
        // Check if the capacity is less than 1
        if (classDto.getCapacity() < 1) {
            throw new ValidationException("Capacity must be at least 1.");
        }

        // Check if the end date is in the past
        if (classDto.getEndDate().isBefore(LocalDate.now())) {
            throw new ValidationException("End date must be in the future.");
        }

        // Check if the end date is before the start date
        if (classDto.getEndDate().isBefore(classDto.getStartDate())) {
            throw new ValidationException("End date must not be before the start date.");
        }

        // Check for overlapping classes in the same date range
        ClubClass conflictingClass = clubClassRepository.findFirstByStartDateBeforeAndEndDateAfter(
                classDto.getEndDate(), classDto.getStartDate());

        if (conflictingClass != null) {
            String errorMessage = String.format(
                    "A class already exists in this date range. " +
                            "Conflicting class: Start Date - %s, End Date - %s",
                    conflictingClass.getStartDate(), conflictingClass.getEndDate());
            throw new ValidationException(errorMessage);
        }
    }

    // Private method to map the saved ClubClass entity to a ClassDto object for returning to the client
    private ClassResponseDto mapToDto(ClubClass clazz) {
        return new ClassResponseDto(
                clazz.getId(),
                clazz.getName(),
                clazz.getStartDate(),
                clazz.getEndDate(),
                clazz.getStartTime(),
                clazz.getDuration(),
                clazz.getCapacity()
        );
    }

    // Method to retrieve all classes
    public List<ClassResponseDto> getAllClasses() {
        // Fetch all classes from the repository
        List<ClubClass> allClasses = clubClassRepository.findAll();

        // Map the list of ClubClass entities to a list of ClassResponseDto objects
        return allClasses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
