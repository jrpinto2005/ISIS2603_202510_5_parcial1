package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    @Autowired
    RecordRepository inscripcionRepository;

    public Double calculateStudentAverage(String login)
    {
        Optional<StudentEntity> optestudiante = estudianteRepository.findByLogin(login);
        StudentEntity estudiante = optestudiante.get();
        List<RecordEntity> records =estudiante.getRecords();
        int count = 0;
        double suma = 0;
        for (RecordEntity record: records)
        {
            count+=1;
            suma+= record.getFinalGrade();
        }
        return suma /count;
    }

    public Double calculateCourseAverage(String courseCode) 
    {
        List <RecordEntity> records = inscripcionRepository.findAll();
        int count = 0;
        double suma = 0;
        for (RecordEntity record :records)
        {
            CourseEntity curso =record.getCourse();
            if (curso.getCourseCode()==courseCode)
            {
                count+=1;
                suma+=record.getFinalGrade();
            }
        }
        return suma/count;
    }

}
