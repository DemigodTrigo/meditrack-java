package com.airtribe.meditrack.test;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.exception.DoctorNotFoundException;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.util.DataStore;

/**
 * Manual tests for the Doctor and DoctorService modules.
 */
public class DoctorTest {

    /**
     * Runs all Doctor module tests.
     */
    public static void run() {

        DataStore<Doctor> doctorStore = new DataStore<>();
        DoctorService doctorService = new DoctorService(doctorStore);

        Doctor doctor1 = new Doctor(
                101,
                "Dr. Kumar",
                45,
                "9876543210",
                "kumar@meditrack.com",
                Specialization.CARDIOLOGIST,
                1500.0,
                true
        );

        Doctor doctor2 = new Doctor(
                102,
                "Dr. Priya",
                38,
                "9876543211",
                "priya@meditrack.com",
                Specialization.DERMATOLOGIST,
                1000.0,
                true
        );

        Doctor doctor3 = new Doctor(
                103,
                "Dr. Arun",
                50,
                "9876543212",
                "arun@meditrack.com",
                Specialization.CARDIOLOGIST,
                2000.0,
                false
        );

        Doctor doctor4 = new Doctor(
                104,
                "Dr. Kumar Raj",
                42,
                "9876543213",
                "kumarraj@meditrack.com",
                Specialization.GENERAL_PHYSICIAN,
                800.0,
                true
        );

        System.out.println("======================================");
        System.out.println("       DOCTOR MODULE TESTS");
        System.out.println("======================================");

        // Add doctors
        System.out.println("\n--- ADD DOCTORS ---");

        doctorService.addDoctor(doctor1);
        doctorService.addDoctor(doctor2);
        doctorService.addDoctor(doctor3);
        doctorService.addDoctor(doctor4);

        System.out.println("Doctors added: " + doctorStore.size());

        // Get doctor
        System.out.println("\n--- GET DOCTOR ---");

        Doctor doctor = doctorService.getDoctorById(101);

        System.out.println(
                "Found: " + doctor.getName()
        );

        // Get all doctors
        System.out.println("\n--- GET ALL DOCTORS ---");

        for (Doctor d : doctorService.getAllDoctors()) {
            System.out.println(
                    d.getId() + " - "
                            + d.getName() + " - "
                            + d.getSpecialization()
            );
        }

        // Update doctor
        System.out.println("\n--- UPDATE DOCTOR ---");

        doctor1.setConsultationFee(1800.0);
        doctorService.updateDoctor(doctor1);

        System.out.println(
                "Updated fee: ₹"
                        + doctorService.getDoctorById(101)
                        .getConsultationFee()
        );

        // Search by ID
        System.out.println("\n--- SEARCH BY ID ---");

        System.out.println(
                doctorService.searchDoctor(102).getName()
        );

        // Search by name
        System.out.println("\n--- SEARCH BY NAME ---");

        doctorService.searchDoctor("kumar")
                .forEach(d -> System.out.println(d.getName()));

        // Search by specialization
        System.out.println("\n--- SEARCH BY SPECIALIZATION ---");

        doctorService
                .findBySpecialization(Specialization.CARDIOLOGIST)
                .forEach(d -> System.out.println(d.getName()));

        // Average fee
        System.out.println("\n--- AVERAGE FEE ---");

        System.out.println(
                "Average fee: ₹"
                        + doctorService.calculateAverageConsultationFee()
        );

        // Highest fee
        System.out.println("\n--- HIGHEST FEE ---");

        Doctor highest =
                doctorService.findHighestConsultationFeeDoctor();

        System.out.println(
                highest.getName()
                        + " - ₹"
                        + highest.getConsultationFee()
        );

        // Delete
        System.out.println("\n--- DELETE DOCTOR ---");

        doctorService.deleteDoctor(104);

        System.out.println(
                "Doctors remaining: "
                        + doctorStore.size()
        );

        // Null validation
        System.out.println("\n--- NULL DOCTOR TEST ---");

        try {
            doctorService.addDoctor(null);
            System.out.println("FAIL");

        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // Invalid ID
        System.out.println("\n--- INVALID ID TEST ---");

        try {
            doctorService.getDoctorById(-1);
            System.out.println("FAIL");

        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // Doctor not found
        System.out.println("\n--- DOCTOR NOT FOUND TEST ---");

        try {
            doctorService.getDoctorById(999);
            System.out.println("FAIL");

        } catch (DoctorNotFoundException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // Blank search
        System.out.println("\n--- BLANK SEARCH TEST ---");

        try {
            doctorService.searchDoctor("   ");
            System.out.println("FAIL");

        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // Equals and hashCode
        System.out.println("\n--- EQUALS / HASHCODE TEST ---");

        Doctor sameIdDoctor = new Doctor(
                101,
                "Different Doctor",
                60,
                "1111111111",
                "different@meditrack.com",
                Specialization.PEDIATRICIAN,
                500.0,
                false
        );

        System.out.println(
                "Equals: " + doctor1.equals(sameIdDoctor)
        );

        System.out.println(
                "HashCode equal: "
                        + (doctor1.hashCode() == sameIdDoctor.hashCode())
        );

        System.out.println("\n======================================");
        System.out.println("       DOCTOR TESTS COMPLETED");
        System.out.println("======================================");
    }
}