package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class StatServiceTest {

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

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
    }

    @Test
    void testFailure() {
        StatsService statsService = new StatsService();
        try
        {
        statsService.calculateCourseAverage("123");
        fail("always fails ...");
        }
        catch(Exception e){
        try 
        {
            statsService.calculateStudentAverage("123");
            fail("always fails ...");
        }
        catch(Exception e2){
        }

    }
}
}
