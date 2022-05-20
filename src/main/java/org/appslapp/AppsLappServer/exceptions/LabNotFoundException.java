package org.appslapp.AppsLappServer.exceptions;

public class LabNotFoundException extends RuntimeException {
    public LabNotFoundException(long labId) {
        super("Lab with id " + labId + " not found");
    }
}
