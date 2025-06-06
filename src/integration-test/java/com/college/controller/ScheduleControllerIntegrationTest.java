package com.college.controller;

import com.college.MainApp;
import com.college.entity.Course;
import com.college.entity.Department;
import com.college.entity.Enrollment;
import com.college.entity.Room;
import com.college.entity.Teacher;
import com.college.entity.Student;
import com.college.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Інтеграційний тест контролера розкладу
@SpringBootTest(
    classes = MainApp.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ScheduleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    private Department department;
    private Teacher teacher;
    private Course course;
    private Room room;
    private Student student;

    // Підготовка тестових даних
    @BeforeEach
    void setUp() {
        // Створення тестової кафедри
        department = new Department();
        department.setName("Комп'ютерні науки");
        department.setLocation("Корпус А");
        departmentService.save(department);

        // Створення тестового викладача
        teacher = new Teacher();
        teacher.setFirstName("Іван");
        teacher.setLastName("Петренко");
        teacher.setDepartment(department);
        teacherService.save(teacher);

        // Створення тестового курсу
        course = new Course();
        course.setCourseName("Програмування Java");
        course.setCredits(3);
        course.setDepartment(department);
        courseService.save(course);

        // Створення тестової аудиторії
        room = new Room();
        room.setRoomNumber("101");
        room.setCapacity(30);
        roomService.save(room);

        // Створення тестового студента
        student = new Student();
        student.setFirstName("Марія");
        student.setLastName("Коваленко");
        student.setDepartment(department);
        studentService.save(student);
    }

    @Test
    void listSchedule_ShouldDisplaySchedulePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/list"))
                .andExpect(model().attributeExists("schedules"));
    }

    // Тест відображення форми додавання заняття
    @Test
    void showAddForm_ShouldDisplayAddForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/add-form"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("classSchedule"));
    }

    // Тест створення нового заняття
    @Test
    void addSchedule_ShouldCreateNewScheduleAndRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/add")
                .param("course", course.getCourseId().toString())
                .param("teacher", teacher.getTeacherId().toString())
                .param("room", room.getRoomId().toString())
                .param("semester", "1")
                .param("year", "2024")
                .param("startTime", "09:00")
                .param("endTime", "10:30")
                .param("students", student.getStudentId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schedule/list"));
    }

    // Тест валідації даних при створенні заняття
    @Test
    void addSchedule_WithInvalidData_ShouldReturnToForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/add")
                .param("course", "")
                .param("teacher", "")
                .param("room", "")
                .param("semester", "")
                .param("year", "невірний")
                .param("startTime", "невірний")
                .param("endTime", "невірний")
                .param("students", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("schedule/add-form"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("classSchedule"));
    }

    @Test
    void deleteEnrollment_ShouldDeleteAndRedirect() throws Exception {
        // Створюємо тестову реєстрацію
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollmentService.save(enrollment);

        // Тестуємо видалення реєстрації
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-enrollment")
                .param("studentName", student.getFirstName() + " " + student.getLastName())
                .param("courseName", course.getCourseName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schedule/list"));
    }

    @Test
    void deleteEnrollment_WithInvalidData_ShouldRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/delete-enrollment")
                .param("studentName", "Неіснуючий Студент")
                .param("courseName", "Неіснуючий Курс"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/schedule/list"));
    }
}