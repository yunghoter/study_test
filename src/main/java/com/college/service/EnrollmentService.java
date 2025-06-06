package com.college.service;

import com.college.entity.Enrollment;
import com.college.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {
    @PersistenceContext
    private EntityManager entityManager;
    
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    @Transactional
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Transactional
    public void deleteById(Long id) {
        enrollmentRepository.deleteById(id);
    }

    @Transactional
    public Enrollment enroll(Enrollment enrollment) {
        // Додаткова бізнес-логіка може бути додана тут
        // наприклад, перевірка місткості курсу, передумов тощо.
        return enrollmentRepository.save(enrollment);
    }

    // Додаємо новий метод для пошуку реєстрації за ім'ям студента та назвою курсу
    @SuppressWarnings("unchecked")
    public List<Enrollment> findByStudentNameAndCourseName(String studentName, String courseName) {
        String[] nameParts = studentName.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        
        String jpql = "SELECT e FROM Enrollment e " +
                     "JOIN e.student s " +
                     "JOIN e.course c " +
                     "WHERE s.firstName = :firstName " +
                     "AND s.lastName = :lastName " +
                     "AND c.courseName = :courseName";
        
        return entityManager.createQuery(jpql)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setParameter("courseName", courseName)
                .getResultList();
    }

    // Додаємо метод для видалення реєстрації за ім'ям студента та назвою курсу
    @Transactional
    public void deleteByStudentNameAndCourseName(String studentName, String courseName) {
        List<Enrollment> enrollments = findByStudentNameAndCourseName(studentName, courseName);
        for (Enrollment enrollment : enrollments) {
            enrollmentRepository.delete(enrollment);
        }
    }
}