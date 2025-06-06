package com.college.service;

import com.college.entity.Teacher;
import com.college.entity.Department;
import com.college.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(1L);
        department.setName("Computer Science");

        teacher = new Teacher();
        teacher.setTeacherId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setDepartment(department);
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher));

        List<Teacher> result = teacherService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTeacherId()).isEqualTo(1L);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");
        assertThat(result.get(0).getDepartment().getName()).isEqualTo("Computer Science");
        verify(teacherRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnTeacher() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTeacherId()).isEqualTo(1L);
        assertThat(result.get().getFirstName()).isEqualTo("John");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
        assertThat(result.get().getDepartment().getName()).isEqualTo("Computer Science");
        verify(teacherRepository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherService.findById(1L);

        assertThat(result).isEmpty();
        verify(teacherRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedTeacher() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher result = teacherService.save(teacher);

        assertThat(result).isNotNull();
        assertThat(result.getTeacherId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        assertThat(result.getDepartment().getName()).isEqualTo("Computer Science");
        verify(teacherRepository).save(teacher);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(teacherRepository).deleteById(1L);

        teacherService.deleteById(1L);

        verify(teacherRepository).deleteById(1L);
    }
}
