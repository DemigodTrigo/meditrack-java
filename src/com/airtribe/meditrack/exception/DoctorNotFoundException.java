package com.airtribe.meditrack.exception;
/**
 * Thrown when a requested doctor cannot be found in the MediTrack system.
 */
public class DoctorNotFoundException extends RuntimeException {
    public  DoctorNotFoundException(String message) {
        super(message);
    }
}
