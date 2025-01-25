package com.gymmanagement.gym_management_application.repository;

import com.gymmanagement.gym_management_application.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMemberName(String memberName);

    List<Booking> findByParticipationDateBetween(LocalDate startDate, LocalDate endDate);

    List<Booking> findByMemberNameAndParticipationDateBetween(String memberName, LocalDate startDate, LocalDate endDate);
}
