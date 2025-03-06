package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.StudentRepository;

@Slf4j
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Transactional
    public StudentEntity createStudent(StudentEntity newStudent) throws RepeatedStudentException 
    {
        log.info("Creando estudiante");
        String login = newStudent.getLogin();
        Optional<StudentEntity> estudiante = studentRepository.findByLogin(login);
        if (!estudiante.isEmpty())
        throw new RepeatedStudentException(login);
        log.info("Creado con exito");
        return studentRepository.save(newStudent);

    }
}
