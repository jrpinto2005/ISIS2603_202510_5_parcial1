package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.services.CourseService;

@DataJpaTest
@Transactional
@Import(CourseService.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateCourse() {
        CourseEntity newEntity = factory.manufacturePojo(CourseEntity.class);
        String codigo = newEntity.getCourseCode();

        try
        {
            CourseEntity storedEntity = courseService.createCourse(newEntity);
            CourseEntity retrieved = entityManager.find(CourseEntity.class, storedEntity.getId());
            assertEquals(codigo, retrieved.getCourseCode(),"No coincide el codigo");
        } catch (RepeatedCourseException e){
        fail("No exception should be thrown");}
    }

    @Test
    void testCreateRepeatedCourse() {
        CourseEntity newEntity = factory.manufacturePojo(CourseEntity.class);
        String codigo = newEntity.getCourseCode();

        CourseEntity repetida = new CourseEntity();
        repetida.setCourseCode(codigo);
        repetida.setName("repetido");

        try
        {
            courseService.createCourse(newEntity);
            courseService.createCourse(repetida);
            fail("An exception must be thrown");
        } catch (Exception e){}
    }
}
