package org.appslapp.AppsLappServer.business.mappers;

import org.appslapp.AppsLappServer.business.Dto.GroupOfExercisesDto;
import org.appslapp.AppsLappServer.business.pojo.GroupOfExercises;

import java.time.Duration;
import java.time.LocalDateTime;

public class GroupMapper {
    public static GroupOfExercisesDto map(GroupOfExercises student) {
        var groupDto = new GroupOfExercisesDto();
        groupDto.setAward(student.getAward());
        groupDto.setEnabled(student.isEnabled());
        groupDto.setExercises(student.getExercises());
        groupDto.setLab(student.getLab());
        groupDto.setName(student.getName());
        groupDto.setMaxStars(student.getMaxStars());
        groupDto.setMinStars(student.getMinStars());
        var deadline = LocalDateTime.parse(student.getDeadline());
        var now = LocalDateTime.now();

        var days = Duration.between(deadline, now).toDays();
        
        groupDto.setDeadline(days);
        
        return groupDto;
    }
}