package org.appslapp.AppsLappServer.business.helper;

import lombok.Getter;
import lombok.Setter;
import org.appslapp.AppsLappServer.business.Dto.ExerciseDto;

@Getter
@Setter
public class ExerciseWithGroupHelper {
    private ExerciseDto exercise;
    private int minStars;
    private int maxStars;
    private long id;
}
