package com.college.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}