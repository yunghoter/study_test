package com.college.service;

import com.college.entity.Student;
import com.college.entity.Department;
import com.college.repository.StudentRepository;
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
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(1L);
        department.setName("Computer Science");

        student = new Student();
        student.setStudentId(1L);
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setDepartment(department);
    }

    @Test
    void findAll_ShouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Student> result = studentService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentId()).isEqualTo(1L);
        assertThat(result.get(0).getFirstName()).isEqualTo("Alice");
        assertThat(result.get(0).getLastName()).isEqualTo("Smith");
        assertThat(result.get(0).getDepartment().getName()).isEqualTo("Computer Science");
        verify(studentRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getStudentId()).isEqualTo(1L);
        assertThat(result.get().getFirstName()).isEqualTo("Alice");
        assertThat(result.get().getLastName()).isEqualTo("Smith");
        assertThat(result.get().getDepartment().getName()).isEqualTo("Computer Science");
        verify(studentRepository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findById(1L);

        assertThat(result).isEmpty();
        verify(studentRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.save(student);

        assertThat(result).isNotNull();
        assertThat(result.getStudentId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Alice");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getDepartment().getName()).isEqualTo("Computer Science");
        verify(studentRepository).save(student);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteById(1L);

        verify(studentRepository).deleteById(1L);
    }
}
