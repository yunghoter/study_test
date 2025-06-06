package com.college.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.entity.ClassSchedule;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
}