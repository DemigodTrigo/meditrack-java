package com.airtribe.meditrack.test;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;

import java.time.LocalDateTime;

public class AppointmentTestRunner {

    public static void main(String[] args) {
        DataStore<Doctor> doctorStore = new DataStore<>();
        DataStore<Patient> patientStore = new DataStore<>();
        DataStore<Appointment> appointmentStore = new DataStore<>();

        DoctorService doctorService = new DoctorService(doctorStore);
        PatientService patientService = new PatientService(patientStore);
        AppointmentService appointmentService =
                new AppointmentService(appointmentStore, doctorService, patientService);

        Doctor drSharma = new Doctor(1, "Dr. Sharma", 45, "9000000001",
                "sharma@meditrack.com", Specialization.CARDIOLOGIST, 800.0, true);
        Patient rahul = new Patient(101, "Rahul Verma", 34, "9111111111",
                "rahul@example.com", "O+", "No known allergies");

        doctorService.addDoctor(drSharma);
        patientService.addPatient(rahul);

        LocalDateTime slot = LocalDateTime.now().plusDays(1).withHour(11).withMinute(0);

        // 1. Create appointment
        Appointment appt = appointmentService.createAppointment(
                1, 101, slot, "Routine cardiac checkup");
        System.out.println("Created: " + appt);

        // 2. Confirm it
        appointmentService.confirmAppointment(appt.getAppointmentId());
        System.out.println("After confirm: " + appointmentService.getAppointmentById(appt.getAppointmentId()));

        // 3. Double-booking should be rejected
        try {
            appointmentService.createAppointment(1, 101, slot, "Follow-up");
            System.out.println("ERROR: conflicting appointment was allowed!");
        } catch (InvalidDataException e) {
            System.out.println("Conflict correctly rejected: " + e.getMessage());
        }

        // 4. Lookups
        System.out.println("By doctor: " + appointmentService.getAppointmentsByDoctor(1));
        System.out.println("By patient: " + appointmentService.getAppointmentsByPatient(101));

        // 5. Cancel
        appointmentService.cancelAppointment(appt.getAppointmentId());
        System.out.println("After cancel: " + appointmentService.getAppointmentById(appt.getAppointmentId()));

        // 6. Not-found path
        try {
            appointmentService.getAppointmentById(9999);
            System.out.println("ERROR: should have thrown AppointmentNotFoundException");
        } catch (AppointmentNotFoundException e) {
            System.out.println("Not-found correctly thrown: " + e.getMessage());
        }
    }
}
