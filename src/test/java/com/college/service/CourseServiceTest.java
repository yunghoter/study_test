package com.college.service;

import com.college.entity.Course;
import com.college.entity.Department;
import com.college.repository.CourseRepository;
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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setDepartmentId(1L);
        department.setName("Computer Science");

        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Introduction to Programming");
        course.setCredits(3);
        course.setDepartment(department);
    }

    @Test
    void findAll_ShouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));

        List<Course> result = courseService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseId()).isEqualTo(1L);
        assertThat(result.get(0).getCourseName()).isEqualTo("Introduction to Programming");
        assertThat(result.get(0).getCredits()).isEqualTo(3);
        assertThat(result.get(0).getDepartment().getName()).isEqualTo("Computer Science");
        verify(courseRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getCourseId()).isEqualTo(1L);
        assertThat(result.get().getCourseName()).isEqualTo("Introduction to Programming");
        assertThat(result.get().getCredits()).isEqualTo(3);
        assertThat(result.get().getDepartment().getName()).isEqualTo("Computer Science");
        verify(courseRepository).findById(1L);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Course> result = courseService.findById(1L);

        assertThat(result).isEmpty();
        verify(courseRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.save(course);

        assertThat(result).isNotNull();
        assertThat(result.getCourseId()).isEqualTo(1L);
        assertThat(result.getCourseName()).isEqualTo("Introduction to Programming");
        assertThat(result.getCredits()).isEqualTo(3);
        assertThat(result.getDepartment().getName()).isEqualTo("Computer Science");
        verify(courseRepository).save(course);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteById(1L);

        verify(courseRepository).deleteById(1L);
    }
}
