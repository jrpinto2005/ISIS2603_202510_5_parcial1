package uniandes.dse.examen1.controllers;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import uniandes.dse.examen1.dto.RecordDTO;
import uniandes.dse.examen1.dto.StudentDTO;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private  StatsService statsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StudentRepository studentRepository;



    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<StudentDTO> getStudents() {
        List<StudentEntity> students = studentRepository.findAll();
        return modelMapper.map(students, new TypeToken<List<StudentDTO>>() {}.getType());

}
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public StudentDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        StudentEntity student = studentRepository.findById(id).get();
        return modelMapper.map(student, StudentDTO.class);
    }
    @GetMapping(value = "/{login}")
    @ResponseStatus(code = HttpStatus.OK)
    public StudentDTO findOne(@PathVariable String login) throws EntityNotFoundException {
        StudentEntity student = studentRepository.findByLogin(login).get();
        return modelMapper.map(student, StudentDTO.class);
    }
    @GetMapping(value = "/{login}/average")
    @ResponseStatus(code = HttpStatus.OK)
    public Double getStudentAverage(@PathVariable String login) {
        return statsService.calculateStudentAverage(login);
    }
    @GetMapping(value = "/{login}/records")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RecordDTO> getStudentRecords(@PathVariable String login) {
        StudentEntity student = studentRepository.findByLogin(login).get();
        return modelMapper.map(student.getRecords(), new TypeToken<List<RecordDTO>>() {}.getType());
    }
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public StudentDTO create(@RequestBody StudentDTO studentDTO) throws Exception {
        StudentEntity student = studentService.createStudent(modelMapper.map(studentDTO, StudentEntity.class));
        return modelMapper.map(student, StudentDTO.class);
    }


    
    


}
