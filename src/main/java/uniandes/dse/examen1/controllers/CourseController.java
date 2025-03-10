package uniandes.dse.examen1.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import uniandes.dse.examen1.dto.CourseDTO;

import uniandes.dse.examen1.dto.StudentDTO;
import uniandes.dse.examen1.entities.CourseEntity;

import uniandes.dse.examen1.repositories.CourseRepository;

import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StatsService;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private  StatsService statsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseRepository courseRepository;



    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<StudentDTO> getCourses() {
        List<CourseEntity> courses = courseRepository.findAll();
        return modelMapper.map(courses, new TypeToken<List<CourseDTO>>() {}.getType());

}
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CourseDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        CourseEntity course = courseRepository.findById(id).get();
        return modelMapper.map(course, CourseDTO.class);
    }
    @GetMapping(value = "/{codigo}")
    @ResponseStatus(code = HttpStatus.OK)
    public CourseDTO findOne(@PathVariable String codigo) throws EntityNotFoundException {
        CourseEntity course = courseRepository.findByCourseCode(codigo).get();
        return modelMapper.map(course, CourseDTO.class);
    }
    @GetMapping(value = "/{codigo}/average")
    @ResponseStatus(code = HttpStatus.OK)
    public Double getCourseAverage(@PathVariable String codigo) {
        return statsService.calculateCourseAverage(codigo);
    }
 
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CourseDTO create(@RequestBody CourseDTO courseDTO) throws Exception {
        CourseEntity course = courseService.createCourse(modelMapper.map(courseDTO, CourseEntity.class));
        return modelMapper.map(course, CourseDTO.class);
    }


    
    


}

    
