package org.appslapp.AppsLappServer.business.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDto {
    private Long id;
    private String name;
    private String gitName;
    private int score;
    private List<String> awards;
    private List<ExerciseStudentDto> exercises;
}
