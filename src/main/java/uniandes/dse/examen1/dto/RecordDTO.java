package uniandes.dse.examen1.dto;

import lombok.Data;

@Data
public class RecordDTO {
    private Long id;
    private Double finalGrade;
    private String semester;
    private CourseDTO course;
    private StudentDTO student;
    
}
