package com.college.service;

import com.college.entity.Course;
import com.college.entity.Enrollment;
import com.college.entity.Student;
import com.college.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;
    private Student student;
    private Course course;
    private javax.persistence.Query query;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(enrollmentService, "entityManager", entityManager);
        
        student = new Student();
        student.setStudentId(1L);
        student.setFirstName("Test");
        student.setLastName("Student");

        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Test Course");
        course.setCredits(3);

        enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        query = mock(javax.persistence.Query.class);
    }

    @Test
    void findAll_ShouldReturnAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(enrollment));

        List<Enrollment> result = enrollmentService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEnrollmentId()).isEqualTo(1L);
        verify(enrollmentRepository).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Optional<Enrollment> result = enrollmentService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getEnrollmentId()).isEqualTo(1L);
        verify(enrollmentRepository).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedEnrollment() {
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.save(enrollment);

        assertThat(result).isNotNull();
        assertThat(result.getEnrollmentId()).isEqualTo(1L);
        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getCourse()).isEqualTo(course);
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void enroll_ShouldSaveEnrollment() {
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.enroll(enrollment);

        assertThat(result).isNotNull();
        assertThat(result.getEnrollmentId()).isEqualTo(1L);
        assertThat(result.getEnrollmentDate()).isNotNull();
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void deleteById_ShouldCallRepository() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        enrollmentService.deleteById(1L);

        verify(enrollmentRepository).deleteById(1L);
    }

    @Test
    void findByStudentNameAndCourseName_ShouldReturnEnrollments() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter("firstName", "Test")).thenReturn(query);
        when(query.setParameter("lastName", "Student")).thenReturn(query);
        when(query.setParameter("courseName", "Test Course")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(enrollment));

        List<Enrollment> result = enrollmentService.findByStudentNameAndCourseName("Test Student", "Test Course");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudent().getFirstName()).isEqualTo("Test");
        assertThat(result.get(0).getStudent().getLastName()).isEqualTo("Student");
        assertThat(result.get(0).getCourse().getCourseName()).isEqualTo("Test Course");
        verify(entityManager).createQuery(anyString());
        verify(query).setParameter("firstName", "Test");
        verify(query).setParameter("lastName", "Student");
        verify(query).setParameter("courseName", "Test Course");
        verify(query).getResultList();
    }

    @Test
    void findByStudentNameAndCourseName_WhenNoResults_ShouldReturnEmptyList() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList());

        List<Enrollment> result = enrollmentService.findByStudentNameAndCourseName("Test Student", "Test Course");

        assertThat(result).isEmpty();
        verify(entityManager).createQuery(anyString());
        verify(query).getResultList();
    }

    @Test
    void deleteByStudentNameAndCourseName_ShouldDeleteFoundEnrollments() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(enrollment));
        doNothing().when(enrollmentRepository).delete(any(Enrollment.class));

        enrollmentService.deleteByStudentNameAndCourseName("Test Student", "Test Course");

        verify(enrollmentRepository).delete(enrollment);
    }

    @Test
    void deleteByStudentNameAndCourseName_WhenNoEnrollments_ShouldNotCallDelete() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList());

        enrollmentService.deleteByStudentNameAndCourseName("Test Student", "Test Course");

        verify(enrollmentRepository, never()).delete(any());
    }
}