package org.appslapp.AppsLappServer.exceptions;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(String exerciseId) {
        super(exerciseId);
    }

    public ExerciseNotFoundException() {
        super();
    }
}
