package com.college.service;

import com.college.entity.Department;
import com.college.repository.DepartmentRepository;
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
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(1L);
        department.setName("Computer Science");
    }

    @Test
    void findAll_ShouldReturnAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));

        List<Department> result = departmentService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartmentId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Computer Science");
        verify(departmentRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getDepartmentId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Computer Science");
        verify(departmentRepository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findById(1L);

        assertThat(result).isEmpty();
        verify(departmentRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.save(department);

        assertThat(result).isNotNull();
        assertThat(result.getDepartmentId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Computer Science");
        verify(departmentRepository).save(department);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteById(1L);

        verify(departmentRepository).deleteById(1L);
    }
}
