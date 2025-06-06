package com.college.config;

import com.college.service.*;
import com.college.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;
    @Mock
    private RoomService roomService;
    @Mock
    private ClassScheduleService classScheduleService;
    @Mock
    private EnrollmentService enrollmentService;
    @Mock
    private Query query;

    @InjectMocks
    private DataInitializer dataInitializer;    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dataInitializer, "entityManager", entityManager);
        // Mock native query for cleanup
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
    }

    @Test
    void run_ShouldInitializeData() throws Exception {
        // Mock department creation
        Department dept1 = new Department();
        dept1.setDepartmentId(1L);
        dept1.setName("Комп'ютерні науки");
        when(departmentService.save(any(Department.class))).thenReturn(dept1);

        // Mock teacher creation
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1L);
        when(teacherService.save(any(Teacher.class))).thenReturn(teacher);

        // Mock student creation
        Student student = new Student();
        student.setStudentId(1L);
        when(studentService.save(any(Student.class))).thenReturn(student);

        // Mock course creation
        Course course = new Course();
        course.setCourseId(1L);
        when(courseService.save(any(Course.class))).thenReturn(course);

        // Mock room creation
        Room room = new Room();
        room.setRoomId(1L);
        when(roomService.save(any(Room.class))).thenReturn(room);

        // Mock schedule creation
        ClassSchedule schedule = new ClassSchedule();
        schedule.setScheduleId(1L);
        when(classScheduleService.save(any(ClassSchedule.class))).thenReturn(schedule);

        // Mock enrollment creation
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);
        when(enrollmentService.save(any(Enrollment.class))).thenReturn(enrollment);        // Execute the run method
        dataInitializer.run(new String[]{});

        // Verify all service calls
        verify(departmentService, atLeast(1)).save(any(Department.class));
        verify(teacherService, atLeast(1)).save(any(Teacher.class));
        verify(studentService, atLeast(1)).save(any(Student.class));
        verify(courseService, atLeast(1)).save(any(Course.class));
        verify(roomService, atLeast(1)).save(any(Room.class));
        verify(classScheduleService, atLeast(1)).save(any(ClassSchedule.class));
        verify(enrollmentService, atLeast(1)).save(any(Enrollment.class));
    }

    @Test
    void cleanupDatabase_ShouldExecuteDropQueries() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        dataInitializer.cleanupDatabase();

        verify(entityManager, atLeast(1)).createNativeQuery(anyString());
        verify(query, atLeast(1)).executeUpdate();
    }
}
