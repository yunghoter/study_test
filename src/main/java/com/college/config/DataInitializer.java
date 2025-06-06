package com.college.config;

import com.college.entity.ClassSchedule;
import com.college.entity.Course;
import com.college.entity.Department;
import com.college.entity.Enrollment;
import com.college.entity.Room;
import com.college.entity.Student;
import com.college.entity.Teacher;
import com.college.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ClassScheduleService classScheduleService;
    @Autowired
    private EnrollmentService enrollmentService;

    @Transactional
    public void cleanupDatabase() {
        // Delete data in order to respect foreign key constraints
        entityManager.createNativeQuery("DELETE FROM enrollments").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM class_schedules").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM students").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM teachers").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM courses").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM rooms").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM departments").executeUpdate();
        
        // Reset sequences
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS enrollments_enrollment_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS class_schedules_schedule_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS students_student_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS teachers_teacher_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS courses_course_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS rooms_room_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE IF EXISTS departments_department_id_seq RESTART WITH 1").executeUpdate();
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Clean up existing data
        cleanupDatabase();
        System.out.println("Database cleaned up successfully!");

        // Create Departments
        Department dept1 = new Department();
        dept1.setName("Комп'ютерні науки");
        dept1.setLocation("Корпус A");
        departmentService.save(dept1);

        Department dept2 = new Department();
        dept2.setName("Математика");
        dept2.setLocation("Корпус B");
        departmentService.save(dept2);

        // Create Teachers
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Іван");
        teacher1.setLastName("Петренко");
        teacher1.setDepartment(dept1);
        teacherService.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Оксана");
        teacher2.setLastName("Коваль");
        teacher2.setDepartment(dept2);
        teacherService.save(teacher2);

        // Create Students
        Student student1 = new Student();
        student1.setFirstName("Аліса");
        student1.setLastName("Мельник");
        student1.setDepartment(dept1);
        studentService.save(student1);

        Student student2 = new Student();
        student2.setFirstName("Богдан");
        student2.setLastName("Іванов");
        student2.setDepartment(dept2);
        studentService.save(student2);

        Student student3 = new Student();
        student3.setFirstName("Катерина");
        student3.setLastName("Левченко");
        student3.setDepartment(dept1);
        studentService.save(student3);

        Student student4 = new Student();
        student4.setFirstName("Дмитро");
        student4.setLastName("Шевченко");
        student4.setDepartment(dept1);
        studentService.save(student4);

        Student student5 = new Student();
        student5.setFirstName("Олена");
        student5.setLastName("Петренко");
        student5.setDepartment(dept2);
        studentService.save(student5);

        // Create Courses
        Course course1 = new Course();
        course1.setCourseName("Вступ до програмування");
        course1.setDepartment(dept1);
        course1.setCredits(3);
        courseService.save(course1);

        Course course2 = new Course();
        course2.setCourseName("Математичний аналіз I");
        course2.setDepartment(dept2);
        course2.setCredits(4);
        courseService.save(course2);

        // Create Rooms
        Room room1 = new Room();
        room1.setRoomNumber("210");
        room1.setCapacity(30);
        roomService.save(room1);

        Room room2 = new Room();
        room2.setRoomNumber("212");
        room2.setCapacity(50);
        roomService.save(room2);

        // Create Class Schedules
        ClassSchedule schedule1 = new ClassSchedule();
        schedule1.setCourse(course1);
        schedule1.setTeacher(teacher1);
        schedule1.setRoom(room1);
        schedule1.setSemester("1");
        schedule1.setYear(2024);
        schedule1.setStartTime(LocalTime.parse("09:00:00"));
        schedule1.setEndTime(LocalTime.parse("10:30:00"));
        classScheduleService.save(schedule1);

        ClassSchedule schedule2 = new ClassSchedule();
        schedule2.setCourse(course2);
        schedule2.setTeacher(teacher2);
        schedule2.setRoom(room2);
        schedule2.setSemester("1");
        schedule2.setYear(2024);
        schedule2.setStartTime(LocalTime.parse("11:00:00"));
        schedule2.setEndTime(LocalTime.parse("12:30:00"));
        classScheduleService.save(schedule2);

        // Create Enrollments for 'Вступ до програмування'
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);  // Аліса
        enrollment1.setCourse(course1);    // Вступ до програмування
        enrollment1.setEnrollmentDate(LocalDate.now());
        enrollmentService.save(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student3);  // Катерина
        enrollment2.setCourse(course1);    // Вступ до програмування
        enrollment2.setEnrollmentDate(LocalDate.now());
        enrollmentService.save(enrollment2);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStudent(student4);  // Дмитро
        enrollment3.setCourse(course1);    // Вступ до програмування
        enrollment3.setEnrollmentDate(LocalDate.now());
        enrollmentService.save(enrollment3);

        // Create Enrollments for 'Математичний аналіз I'
        Enrollment enrollment4 = new Enrollment();
        enrollment4.setStudent(student2);  // Богдан
        enrollment4.setCourse(course2);    // Математичний аналіз I
        enrollment4.setEnrollmentDate(LocalDate.now());
        enrollmentService.save(enrollment4);

        Enrollment enrollment5 = new Enrollment();
        enrollment5.setStudent(student5);  // Олена
        enrollment5.setCourse(course2);    // Математичний аналіз I
        enrollment5.setEnrollmentDate(LocalDate.now());
        enrollmentService.save(enrollment5);

        System.out.println("Дані успішно збережено!");
    }
}