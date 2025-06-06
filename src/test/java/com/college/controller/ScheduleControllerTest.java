package com.college.controller;

import com.college.entity.*;
import com.college.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {

    @Mock
    private ScheduleService scheduleService;
    @Mock
    private StudentService studentService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private CourseService courseService;
    @Mock
    private RoomService roomService;
    @Mock
    private ClassScheduleService classScheduleService;
    @Mock
    private EnrollmentService enrollmentService;
    @Mock
    private Model model;

    @InjectMocks
    private ScheduleController scheduleController;

    private ClassSchedule classSchedule;
    private Course course;
    private Teacher teacher;
    private Room room;
    private Student student;
    private List<ScheduleInfoDTO> scheduleInfoList;

    @BeforeEach
    void setUp() {
        Department dept = new Department();
        dept.setDepartmentId(1L);
        dept.setName("Computer Science");

        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Programming");
        course.setDepartment(dept);

        teacher = new Teacher();
        teacher.setTeacherId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setDepartment(dept);

        room = new Room();
        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setCapacity(30);

        student = new Student();
        student.setStudentId(1L);
        student.setFirstName("Alice");
        student.setLastName("Smith");
        student.setDepartment(dept);

        classSchedule = new ClassSchedule();
        classSchedule.setScheduleId(1L);
        classSchedule.setCourse(course);
        classSchedule.setTeacher(teacher);
        classSchedule.setRoom(room);
        classSchedule.setSemester("1");
        classSchedule.setYear(2024);
        classSchedule.setStartTime(LocalTime.of(9, 0));
        classSchedule.setEndTime(LocalTime.of(10, 30));

        ScheduleInfoDTO scheduleInfo = new ScheduleInfoDTO(
            student.getFirstName(), student.getLastName(),
            teacher.getFirstName(), teacher.getLastName(),
            course.getCourseName(), dept.getName(),
            room.getRoomNumber(), room.getCapacity(),
            classSchedule.getSemester(), classSchedule.getYear(),
            classSchedule.getStartTime(), classSchedule.getEndTime()
        );
        scheduleInfoList = Arrays.asList(scheduleInfo);
    }

    @Test
    void listSchedule_ShouldReturnScheduleListView() {
        when(scheduleService.getScheduleInfo()).thenReturn(scheduleInfoList);

        String viewName = scheduleController.listSchedule(model);

        assertThat(viewName).isEqualTo("schedule/list");
        verify(model).addAttribute("schedules", scheduleInfoList);
    }

    @Test
    void showAddForm_ShouldReturnAddFormView() {
        when(studentService.findAll()).thenReturn(Arrays.asList(student));
        when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
        when(courseService.findAll()).thenReturn(Arrays.asList(course));
        when(roomService.findAll()).thenReturn(Arrays.asList(room));

        String viewName = scheduleController.showAddForm(model);

        assertThat(viewName).isEqualTo("schedule/add-form");
        verify(model).addAttribute(eq("students"), any());
        verify(model).addAttribute(eq("teachers"), any());
        verify(model).addAttribute(eq("courses"), any());
        verify(model).addAttribute(eq("rooms"), any());
        verify(model).addAttribute(eq("classSchedule"), any(ClassSchedule.class));
    }

    @Test
    void addSchedule_WithValidData_ShouldRedirectToList() {
        when(courseService.findById(1L)).thenReturn(Optional.of(course));
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
        when(roomService.findById(1L)).thenReturn(Optional.of(room));
        when(classScheduleService.save(any(ClassSchedule.class))).thenReturn(classSchedule);
        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        String viewName = scheduleController.addSchedule(
            1L, 1L, 1L,
            "1", "2024",
            "09:00", "10:30",
            Arrays.asList(1L),
            model
        );

        assertThat(viewName).isEqualTo("redirect:/schedule/list");
        verify(classScheduleService).save(any(ClassSchedule.class));
        verify(enrollmentService).save(any(Enrollment.class));
    }

    @Test
    void addSchedule_WithInvalidData_ShouldReturnToForm() {
        String viewName = scheduleController.addSchedule(
            null, null, null,
            null, null,
            null, null,
            null,
            model
        );

        assertThat(viewName).isEqualTo("schedule/add-form");
        verify(model).addAttribute(eq("students"), any());
        verify(model).addAttribute(eq("teachers"), any());
        verify(model).addAttribute(eq("courses"), any());
        verify(model).addAttribute(eq("rooms"), any());
        verify(model).addAttribute(eq("classSchedule"), any(ClassSchedule.class));
    }
}
