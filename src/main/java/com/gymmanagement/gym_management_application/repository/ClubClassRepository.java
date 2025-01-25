package com.gymmanagement.gym_management_application.repository;

import com.gymmanagement.gym_management_application.entity.ClubClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ClubClassRepository extends JpaRepository<ClubClass, Long> {
    boolean existsByStartDateBeforeAndEndDateAfter(LocalDate endDate, LocalDate startDate);

    ClubClass findFirstByStartDateBeforeAndEndDateAfter(LocalDate endDate, LocalDate startDate);
}
