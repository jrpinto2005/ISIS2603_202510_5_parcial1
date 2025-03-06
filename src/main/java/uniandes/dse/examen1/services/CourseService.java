package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;

@Slf4j
@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public CourseEntity createCourse(CourseEntity newCourse) throws RepeatedCourseException
     {
        log.info("Creando estudiante");
        String codigo = newCourse.getCourseCode();
        Optional<CourseEntity> curso = courseRepository.findByCourseCode(codigo);
        if (!curso.isEmpty())
        throw new RepeatedCourseException(codigo);
        log.info("Creado con exito");
        return courseRepository.save(newCourse);
    }
}
