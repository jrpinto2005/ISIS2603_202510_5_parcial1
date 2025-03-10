package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.services.RecordService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private String login;
    private String courseCode;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse = courseService.createCourse(newCourse);
        courseCode = newCourse.getCourseCode();

        StudentEntity newStudent = factory.manufacturePojo(StudentEntity.class);
        newStudent = studentService.createStudent(newStudent);
        login = newStudent.getLogin();
    }

    /**
     * Tests the normal creation of a record for a student in a course
          * @throws InvalidRecordException 
          */
         @Test
         void testCreateRecord() throws InvalidRecordException {
        RecordEntity newRecord = recordService.createRecord(login, courseCode, 4.5, "2025-1");
        StudentEntity student = newRecord.getEstudiante();
        CourseEntity course = newRecord.getCourse();
        StudentEntity newStudent = studentRepository.findByLogin(login).get();
        CourseEntity newCourse = courseRepository.findByCourseCode(courseCode).get();
        assertEquals(login, student.getLogin());
        assertEquals(student.getCourses(), newStudent.getCourses());
        assertEquals(student.getId(), newStudent.getId());
        assertEquals(student.getName(), newStudent.getName());
        assertEquals(courseCode, course.getCourseCode());
        assertEquals(course.getName(), newCourse.getName());
        assertEquals(course.getId(), newCourse.getId());
        assertEquals(newRecord.getFinalGrade(), 4.5);
        assertEquals(newRecord.getSemester(), "2025-1");


    }

    /**
     * Tests the creation of a record when the login of the student is wrong
     */
    @Test
    void testCreateRecordMissingStudent() {
        try
        {
            RecordEntity newRecord = recordService.createRecord("incorrecto", courseCode, 4.5, "2025-1");
            fail("El estudiante no existe");
        } catch (InvalidRecordException e){
            assertEquals("El estudiante no existe", e.getMessage());
        }
    }

    /**
     * Tests the creation of a record when the course code is wrong
     */
    @Test
    void testCreateInscripcionMissingCourse() {
        try
        {
            RecordEntity newRecord = recordService.createRecord(login, "incorrecto", 4.5, "2025-1");
            fail("El curso no existe");
        } catch (InvalidRecordException e){
            assertEquals("El curso no existe", e.getMessage());
        }
    }

    /**
     * Tests the creation of a record when the grade is not valid
     */
    @Test
    void testCreateInscripcionWrongGrade() {
        try 
        {
            RecordEntity newRecord = recordService.createRecord(login, courseCode, 0.5, "2025-1");
            fail("La calificacion no es valida");
        } catch (InvalidRecordException e){
            assertEquals("La calificacion no es valida", e.getMessage());
        }
        try 
        {
            RecordEntity newRecord = recordService.createRecord(login, courseCode, 5.5, "2025-1");
            fail("La calificacion no es valida");
        } catch (InvalidRecordException e){
            assertEquals("La calificacion no es valida", e.getMessage());
        }
        
    }

    /**
     * Tests the creation of a record when the student already has a passing grade
     * for the course
     */
    @Test
    void testCreateInscripcionRepetida1() {
        try 
        {
            RecordEntity newRecord = recordService.createRecord(login, courseCode, 4.0, "2025-1");
            RecordEntity newRecord2 = recordService.createRecord(login, courseCode, 4.5, "2025-2");
            fail("Este curso ya fue aprobado");
        } catch (InvalidRecordException e){
            assertEquals("Este curso ya fue aprobado", e.getMessage());
        }
    }

    /**
     * Tests the creation of a record when the student already has a record for the
     * course, but he has not passed the course yet.
          * @throws InvalidRecordException 
          */
         @Test
         void testCreateInscripcionRepetida2() throws InvalidRecordException 
    {
     
        RecordEntity newRecord = recordService.createRecord(login, courseCode, 2.0, "2025-1");
        RecordEntity newRecord2 = recordService.createRecord(login, courseCode, 4.5, "2025-2");
        List<RecordEntity> records = studentRepository.findByLogin(login).get().getRecords();
        assertEquals(2, records.size());
        assertEquals(2.0, records.get(0).getFinalGrade());
        assertEquals(4.5, records.get(1).getFinalGrade());
        assertEquals(courseCode, records.get(0).getCourse().getCourseCode());
        assertEquals(courseCode, records.get(1).getCourse().getCourseCode());
        assertEquals("2025-1", records.get(0).getSemester());
        assertEquals("2025-2", records.get(1).getSemester());

    }
}
