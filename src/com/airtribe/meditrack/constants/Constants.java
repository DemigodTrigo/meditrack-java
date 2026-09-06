package com.airtribe.meditrack.constants;

public final class Constants {

    private Constants() {
        // Prevent object creation
    }

    // -------------------------
    // Billing
    // -------------------------

    public static final double TAX_RATE = 0.18;

    // -------------------------
    // Appointment
    // -------------------------

    public static final int APPOINTMENT_DURATION_MINUTES = 30;

    // -------------------------
    // Validation
    // -------------------------

    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 120;

    // -------------------------
    // File Paths
    // -------------------------

    public static final String DOCTOR_FILE = "doctors.csv";
    public static final String PATIENT_FILE = "patients.csv";
    public static final String APPOINTMENT_FILE = "appointments.csv";
    public static final String BILL_FILE = "bills.csv";

    // -------------------------
    // Application
    // -------------------------

    public static final String APPLICATION_NAME = "MediTrack";
}