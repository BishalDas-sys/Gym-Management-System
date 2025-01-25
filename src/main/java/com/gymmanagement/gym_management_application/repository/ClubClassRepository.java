package com.gymmanagement.gym_management_application.repository;

import com.gymmanagement.gym_management_application.entity.ClubClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubClassRepository extends JpaRepository<ClubClass, Long> {
}
