package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException 
    {
     Optional<StudentEntity> estudiante = studentRepository.findByLogin(loginStudent);
     if (estudiante.isEmpty())   
     throw new InvalidRecordException("El estudiante no existe");
     Optional<CourseEntity> curso = courseRepository.findByCourseCode(courseCode);
     if (courseCode.isEmpty())
     throw new InvalidRecordException("El curso no existe");
     if (grade<1.5||grade>5)
     throw new InvalidRecordException("La calificacion no es valida");
     if (grade>=3)
     {
        List<RecordEntity> records = estudiante.get().getRecords();
        for (RecordEntity record : records)
        {
            if (record.getCourse() == curso.get() && record.getFinalGrade()>=3)
            throw new InvalidRecordException("Este curso ya fue aprobado");
        }
     }
     RecordEntity newRecord = new RecordEntity();
     newRecord.setCourse(curso.get()); 
     newRecord.setEstudiante(estudiante.get());
     newRecord.setFinalGrade(grade);
     newRecord.setSemester(semester);
     log.info("Creado y guardado con exito");
     return recordRepository.save(newRecord);
    }
}
