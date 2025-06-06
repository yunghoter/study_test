package com.college.service;

import com.college.entity.ClassSchedule;
import com.college.repository.ClassScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassScheduleServiceTest {

    @Mock
    private ClassScheduleRepository classScheduleRepository;

    @InjectMocks
    private ClassScheduleService classScheduleService;

    private ClassSchedule classSchedule;

    @BeforeEach
    void setUp() {
        classSchedule = new ClassSchedule();
        classSchedule.setScheduleId(1L);
        classSchedule.setSemester("1");
        classSchedule.setYear(2024);
        classSchedule.setStartTime(LocalTime.of(9, 0));
        classSchedule.setEndTime(LocalTime.of(10, 30));
    }

    @Test
    void findAll_ShouldReturnAllSchedules() {
        when(classScheduleRepository.findAll()).thenReturn(Arrays.asList(classSchedule));

        List<ClassSchedule> result = classScheduleService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScheduleId()).isEqualTo(1);
        verify(classScheduleRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnSchedule() {
        when(classScheduleRepository.findById(1L)).thenReturn(Optional.of(classSchedule));

        Optional<ClassSchedule> result = classScheduleService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getScheduleId()).isEqualTo(1);
        verify(classScheduleRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedSchedule() {
        when(classScheduleRepository.save(any(ClassSchedule.class))).thenReturn(classSchedule);

        ClassSchedule result = classScheduleService.save(classSchedule);

        assertThat(result).isNotNull();
        assertThat(result.getScheduleId()).isEqualTo(1);
        verify(classScheduleRepository).save(classSchedule);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(classScheduleRepository).deleteById(1L);

        classScheduleService.deleteById(1L);

        verify(classScheduleRepository).deleteById(1L);
    }
}