package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.PatientNotFoundException;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;

import java.util.List;

public class PatientTest {

    public static void run() {

        System.out.println("\n===== PATIENT TEST =====");

        DataStore<Patient> patientStore = new DataStore<>();
        PatientService patientService =
                new PatientService(patientStore);

        // -------------------------------------------------
        // ADD PATIENT
        // -------------------------------------------------

        Patient patient1 = new Patient(
                201,
                "Rahul Sharma",
                32,
                "9000000021",
                "rahul@gmail.com",
                "O+",
                "No known allergies"
        );

        Patient patient2 = new Patient(
                202,
                "Priya Verma",
                28,
                "9000000022",
                "priya@gmail.com",
                "A+",
                "Asthma"
        );

        patientService.addPatient(patient1);
        patientService.addPatient(patient2);

        System.out.println("Patients added successfully.");

        // -------------------------------------------------
        // GET BY ID
        // -------------------------------------------------

        Patient found =
                patientService.getPatientById(201);

        System.out.println(
                "Found patient: " + found
        );

        // -------------------------------------------------
        // GET ALL
        // -------------------------------------------------

        List<Patient> patients =
                patientService.getAllPatients();

        System.out.println(
                "Total patients: " + patients.size()
        );

        // -------------------------------------------------
        // SEARCH
        // -------------------------------------------------

        List<Patient> searchResults =
                patientService.searchPatients("Rahul");

        System.out.println(
                "Search results for Rahul: " +
                        searchResults
        );

        // -------------------------------------------------
        // UPDATE
        // -------------------------------------------------

        patient1.setAge(33);
        patient1.setMedicalHistory(
                "No known allergies - regular checkup"
        );

        patientService.updatePatient(patient1);

        Patient updated =
                patientService.getPatientById(201);

        System.out.println(
                "Updated patient: " + updated
        );

        // -------------------------------------------------
        // COUNT
        // -------------------------------------------------

        System.out.println(
                "Patient count: " +
                        patientService.getPatientCount()
        );

        // -------------------------------------------------
        // NOT FOUND TEST
        // -------------------------------------------------

        try {

            patientService.getPatientById(9999);

            System.out.println(
                    "Not-found test failed."
            );

        } catch (PatientNotFoundException e) {

            System.out.println(
                    "Not-found test passed: " +
                            e.getMessage()
            );
        }

        // -------------------------------------------------
        // DELETE
        // -------------------------------------------------

        patientService.deletePatient(202);

        System.out.println(
                "Patient 202 deleted."
        );

        System.out.println(
                "Remaining patients: " +
                        patientService.getAllPatients().size()
        );

        System.out.println(
                "===== PATIENT TEST COMPLETED ====="
        );
    }

    public static void main(String[] args) {
        run();
    }
}