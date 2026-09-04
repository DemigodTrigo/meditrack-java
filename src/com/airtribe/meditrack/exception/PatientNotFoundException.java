package com.airtribe.meditrack.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(long patientId) {
        super("Patient with ID " + patientId + " not found.");
    }

    public PatientNotFoundException(String message) {
        super(message);
    }
}