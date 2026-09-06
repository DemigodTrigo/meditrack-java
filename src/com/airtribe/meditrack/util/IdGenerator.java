package com.airtribe.meditrack.util;

public final class IdGenerator {

    private static long doctorId = 1;
    private static long patientId = 101;
    private static long appointmentId = 1001;
    private static long billId = 5001;

    private IdGenerator() {

    }

    public static synchronized long nextDoctorId() {
        return doctorId++;
    }

    public static synchronized long nextPatientId() {
        return patientId++;
    }

    public static synchronized long nextAppointmentId() {
        return appointmentId++;
    }

    public static synchronized long nextBillId() {
        return billId++;
    }
}