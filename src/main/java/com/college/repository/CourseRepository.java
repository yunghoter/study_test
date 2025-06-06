package com.college.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}