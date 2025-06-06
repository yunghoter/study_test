package com.college.service;

import com.college.entity.ClassSchedule;
import com.college.repository.ClassScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassScheduleService {
    private final ClassScheduleRepository classScheduleRepository;

    @Autowired
    public ClassScheduleService(ClassScheduleRepository classScheduleRepository) {
        this.classScheduleRepository = classScheduleRepository;
    }

    public List<ClassSchedule> findAll() {
        return classScheduleRepository.findAll();
    }

    public Optional<ClassSchedule> findById(Long id) {
        return classScheduleRepository.findById(id);
    }

    public ClassSchedule save(ClassSchedule classSchedule) {
        return classScheduleRepository.save(classSchedule);
    }

    public void deleteById(Long id) {
        classScheduleRepository.deleteById(id);
    }
}