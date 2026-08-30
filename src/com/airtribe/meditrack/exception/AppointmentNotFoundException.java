package com.airtribe.meditrack.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(long appointmentId) {
        super("Appointment not found with ID: " + appointmentId);
    }

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}