package uniandes.dse.examen1.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String name;
    private Integer credits;

}
