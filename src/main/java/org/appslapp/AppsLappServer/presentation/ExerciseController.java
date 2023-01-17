package org.appslapp.AppsLappServer.presentation;

import org.appslapp.AppsLappServer.business.Dto.ExerciseDto;
import org.appslapp.AppsLappServer.business.helper.ExerciseUpdateHelper;
import org.appslapp.AppsLappServer.business.helper.ExerciseWithGroupHelper;
import org.appslapp.AppsLappServer.business.mappers.ExerciseMapper;
import org.appslapp.AppsLappServer.business.pojo.exercise.Exercise;
import org.appslapp.AppsLappServer.business.pojo.exercise.ExerciseService;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercises;
import org.appslapp.AppsLappServer.business.pojo.groupOfExercises.GroupOfExercisesService;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercise/")
public class ExerciseController {
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final GroupOfExercisesService groupOfExercisesService;

    @Autowired
    public ExerciseController(UserService userService,
                         ExerciseService exerciseService,
                         GroupOfExercisesService groupOfExercisesService) {
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.groupOfExercisesService = groupOfExercisesService;
    }

    @DeleteMapping("{exerciseName}")
    public Long deleteExercise(@PathVariable String exerciseName) {
        exerciseService.deleteExercise(exerciseName);
        return 1L;
    }

    @GetMapping("{exerciseName}")
    public ExerciseDto getExercise(@PathVariable String exerciseName) {
        return ExerciseMapper.map(exerciseService.getExerciseByName(exerciseName));
    }

    @GetMapping
    public List<ExerciseDto> getAllExercises() {;
        return exerciseService.getAllExercises().stream().map(ExerciseMapper::map).collect(Collectors.toList());
    }

    @PostMapping("updateScore")
    public Long updateScore(@RequestBody ExerciseUpdateHelper body) {
        var user = userService.getUserById(body.getStudentId());
        user.setScore(user.getScore() + body.getScore());

        var exercise = exerciseService.getExerciseByName(body.getExerciseName());
        user.getDoneExercises().add(exercise);
        userService.update(user);
        return 1L;
    }

    @PostMapping
    public Long createExercise(@RequestBody ExerciseWithGroupHelper body) {
        var exercise = new Exercise();

        try {
            try {
                exercise = exerciseService.getExercise(body.getId());
            } catch (Exception ignored) {

            }

            var group = groupOfExercisesService.getGroupOfExercisesByName(
                    body.getExercise().getGroupName());

            group.getExercises().remove(exercise);

            exercise.setDescription(body.getExercise().getDescription());
            exercise.setName(body.getExercise().getName());
            exercise.setRequiredStars(body.getExercise().getRequiredStars());

            group.getExercises().add(exercise);

            exercise.setGroupOfExercises(group);
            return exerciseService.save(exercise);
        } catch (Exception ignored) {
            var group = new GroupOfExercises();
            group.setName(body.getExercise().getGroupName());

            exercise.setDescription(body.getExercise().getDescription());
            exercise.setName(body.getExercise().getName());
            exercise.setRequiredStars(body.getExercise().getRequiredStars());

            group.setExercises(List.of(exercise));
            group.setMinStars(body.getMinStars());
            group.setMaxStars(body.getMaxStars());
            group.setAward(body.getAward());
            group.setDeadline(body.getDeadline());
            exercise.setGroupOfExercises(group);
            return exerciseService.save(exercise, groupOfExercisesService);
        }
    }
}
