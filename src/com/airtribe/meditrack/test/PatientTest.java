package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.exception.PatientNotFoundException;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;

public class PatientTest {

    public static void run() {

        DataStore<Patient> patientStore = new DataStore<>();
        PatientService patientService = new PatientService(patientStore);

        Patient patient1 = new Patient(
                101,
                "Rahul Verma",
                34,
                "9111111111",
                "rahul@example.com",
                "O+",
                "No known allergies"
        );

        Patient patient2 = new Patient(
                102,
                "Priya Sharma",
                34,
                "9222222222",
                "priya@example.com",
                "A+",
                "Asthma"
        );

        Patient patient3 = new Patient(
                103,
                "Amit Das",
                50,
                "9333333333",
                "amit@example.com",
                "B+",
                "Diabetes"
        );

        System.out.println("======================================");
        System.out.println("       PATIENT MODULE TESTS");
        System.out.println("======================================");

        // 1. Add patients
        System.out.println("\n--- ADD PATIENTS ---");

        patientService.addPatient(patient1);
        patientService.addPatient(patient2);
        patientService.addPatient(patient3);

        System.out.println(
                "Patients added: " + patientStore.size()
        );

        // 2. Get by ID
        System.out.println("\n--- GET PATIENT ---");

        Patient found = patientService.getPatientById(101);

        System.out.println(
                "Found: " + found.getName()
        );

        // 3. Get all
        System.out.println("\n--- ALL PATIENTS ---");

        patientService.getAllPatients()
                .forEach(System.out::println);

        // 4. Update
        System.out.println("\n--- UPDATE PATIENT ---");

        patient1.setPhone("9999999999");
        patient1.setMedicalHistory("No known allergies - updated");

        patientService.updatePatient(patient1);

        System.out.println(
                "Updated phone: "
                        + patientService.getPatientById(101).getPhone()
        );

        // 5. Search by ID
        System.out.println("\n--- SEARCH BY ID ---");

        System.out.println(
                patientService.searchPatient(102)
        );

        // 6. Search by name
        System.out.println("\n--- SEARCH BY NAME ---");

        patientService.searchPatient("rah")
                .forEach(System.out::println);

        // 7. Search by age
        System.out.println("\n--- SEARCH BY AGE ---");

        patientService.searchPatient(34)
                .forEach(System.out::println);

        // 8. Clone
        System.out.println("\n--- CLONE TEST ---");

        Patient clonedPatient = patient1.clone();

        clonedPatient.setName("Rahul Clone");
        clonedPatient.setMedicalHistory("Clone medical history");

        System.out.println("Original: " + patient1);
        System.out.println("Clone:    " + clonedPatient);

        System.out.println(
                "Original unchanged: "
                        + !patient1.getName().equals(clonedPatient.getName())
        );

        // 9. Equals / HashCode
        System.out.println("\n--- EQUALS / HASHCODE TEST ---");

        Patient sameIdPatient = new Patient(
                101,
                "Different Name",
                60,
                "1111111111",
                "different@example.com",
                "AB+",
                "Different history"
        );

        System.out.println(
                "Equals: " + patient1.equals(sameIdPatient)
        );

        System.out.println(
                "HashCode equal: "
                        + (patient1.hashCode() == sameIdPatient.hashCode())
        );

        // 10. Delete
        System.out.println("\n--- DELETE PATIENT ---");

        patientService.deletePatient(103);

        System.out.println(
                "Patients remaining: " + patientStore.size()
        );

        // 11. Null validation
        System.out.println("\n--- NULL PATIENT TEST ---");

        try {
            patientService.addPatient(null);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 12. Invalid age
        System.out.println("\n--- INVALID AGE TEST ---");

        try {
            Patient invalidPatient = new Patient(
                    200,
                    "Invalid Patient",
                    150,
                    "9000000000",
                    "invalid@example.com",
                    "O+",
                    "None"
            );

            patientService.addPatient(invalidPatient);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 13. Blank name
        System.out.println("\n--- BLANK NAME TEST ---");

        try {
            Patient invalidPatient = new Patient(
                    201,
                    "   ",
                    30,
                    "9000000000",
                    "invalid@example.com",
                    "O+",
                    "None"
            );

            patientService.addPatient(invalidPatient);
            System.out.println("FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 14. Duplicate ID
        System.out.println("\n--- DUPLICATE ID TEST ---");

        try {
            Patient duplicate = new Patient(
                    101,
                    "Duplicate",
                    25,
                    "8888888888",
                    "duplicate@example.com",
                    "A+",
                    "None"
            );

            patientService.addPatient(duplicate);
            System.out.println("FAIL");
        } catch (InvalidDataException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 15. Patient not found
        System.out.println("\n--- PATIENT NOT FOUND TEST ---");

        try {
            patientService.getPatientById(999);
            System.out.println("FAIL");
        } catch (PatientNotFoundException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        System.out.println("\n======================================");
        System.out.println("       PATIENT TESTS COMPLETED");
        System.out.println("======================================");
    }
}