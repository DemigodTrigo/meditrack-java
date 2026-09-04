package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.DoctorNotFoundException;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the MediTrack console application.
 *
 * This class provides a command-line interface for managing doctors, patients, appointments, and billing within the MediTrack system.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final DataStore<Doctor> doctorStore =
            new DataStore<>();

    private static final DoctorService doctorService =
            new DoctorService(doctorStore);

    private static final DataStore<Patient> patientStore =
            new DataStore<>();

    private static final PatientService patientService =
            new PatientService(patientStore);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            printMainMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    doctorMenu();
                    break;

                case 2:
                    patientMenu();
                    break;

                case 3:

                    break;

                case 4:

                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting MediTrack...");
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }

        scanner.close();
    }

    /**
     * Displays the main MediTrack menu.
     */
    private static void printMainMenu() {

        System.out.println();
        System.out.println("=================================");
        System.out.println("          MEDI TRACK");
        System.out.println("=================================");
        System.out.println("1. Doctor Management");
        System.out.println("2. Patient Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing");
        System.out.println("5. Exit");
        System.out.println("=================================");
    }

    /**
     * Displays the Doctor management menu.
     */
    private static void doctorMenu() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("       DOCTOR MANAGEMENT");
            System.out.println("=================================");
            System.out.println("1. Add Doctor");
            System.out.println("2. Get All Doctors");
            System.out.println("3. Update Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Search Doctor By ID");
            System.out.println("6. Search Doctor By Name");
            System.out.println("7. Search By Specialization");
            System.out.println("8. Calculate Average Consultation Fee");
            System.out.println("9. Find Highest Consultation Fee");
            System.out.println("10. Appointment Analytics By Doctor");
            System.out.println("11. Back");
            System.out.println("=================================");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addDoctor();
                    break;

                case 2:
                    getAllDoctors();
                    break;

                case 3:
                    updateDoctor();
                    break;

                case 4:
                    deleteDoctor();
                    break;

                case 5:
                    searchDoctorById();
                    break;

                case 6:
                    searchDoctorByName();
                    break;

                case 7:
                    searchBySpecialization();
                    break;

                case 8:
                    calculateAverageFee();
                    break;

                case 9:
                    findHighestFeeDoctor();
                    break;

                case 10:
                    appointmentAnalytics();
                    break;

                case 11:
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    private static void appointmentAnalytics() {
    }

    /**
     * Adds a new Doctor using console input.
     */
    private static void addDoctor() {

        System.out.println("\n--- ADD DOCTOR ---");

        long id = readLong("Enter doctor ID: ");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        int age = readInt("Enter age: ");

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Specialization specialization = readSpecialization();

        double fee = readDouble("Enter consultation fee: ");

        boolean available = readBoolean("Is doctor available? (true/false): ");

        Doctor doctor = new Doctor(
                id,
                name,
                age,
                phone,
                email,
                specialization,
                fee,
                available
        );

        try {

            doctorService.addDoctor(doctor);

            System.out.println(
                    "Doctor added successfully."
            );

        } catch (DoctorNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all doctors.
     */
    private static void getAllDoctors() {

        System.out.println("\n--- ALL DOCTORS ---");

        List<Doctor> doctors = doctorService.getAllDoctors();

        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }

        for (Doctor doctor : doctors) {
            printDoctor(doctor);
        }
    }

    /**
     * Updates an existing Doctor.
     */
    private static void updateDoctor() {

        System.out.println("\n--- UPDATE DOCTOR ---");

        long id = readLong("Enter doctor ID: ");

        try {

            Doctor doctor = doctorService.getDoctorById(id);

            System.out.println("Current doctor: " + doctor.getName());

            System.out.print("Enter new name: ");
            String name = scanner.nextLine();

            int age = readInt("Enter new age: ");

            System.out.print("Enter new phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            Specialization specialization = readSpecialization();

            double fee = readDouble("Enter new consultation fee: ");

            boolean available = readBoolean("Is doctor available? (true/false): ");

            doctor.setName(name);
            doctor.setAge(age);
            doctor.setPhone(phone);
            doctor.setEmail(email);
            doctor.setSpecialization(specialization);
            doctor.setConsultationFee(fee);
            doctor.setAvailable(available);

            doctorService.updateDoctor(doctor);

            System.out.println("Doctor updated successfully.");

        } catch (RuntimeException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Deletes a Doctor by ID.
     */
    private static void deleteDoctor() {

        System.out.println("\n--- DELETE DOCTOR ---");

        long id = readLong("Enter doctor ID: ");

        try {

            doctorService.deleteDoctor(id);

            System.out.println("Doctor deleted successfully.");

        } catch (RuntimeException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Searches for a Doctor using the Doctor ID.
     */
    private static void searchDoctorById() {

        System.out.println("\n--- SEARCH DOCTOR BY ID ---");

        long id = readLong("Enter doctor ID: ");

        try {

            Doctor doctor = doctorService.searchDoctor(id);

            printDoctor(doctor);

        } catch (RuntimeException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Searches Doctors by name.
     */
    private static void searchDoctorByName() {

        System.out.println("\n--- SEARCH BY NAME ---");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        try {

            List<Doctor> doctors = doctorService.searchDoctor(name);

            if (doctors.isEmpty()) {
                System.out.println("No matching doctors found.");
                return;
            }

            for (Doctor doctor : doctors) {
                printDoctor(doctor);
            }

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Searches Doctors by specialization.
     */
    private static void searchBySpecialization() {

        System.out.println(
                "\n--- SEARCH BY SPECIALIZATION ---"
        );

        try {

            Specialization specialization = readSpecialization();

            List<Doctor> doctors = doctorService.findBySpecialization(specialization);

            if (doctors.isEmpty()) {
                System.out.println("No doctors found.");
                return;
            }

            for (Doctor doctor : doctors) {
                printDoctor(doctor);
            }

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Calculates and displays the average consultation fee.
     */
    private static void calculateAverageFee() {

        System.out.println(
                "\n--- AVERAGE CONSULTATION FEE ---"
        );

        double average = doctorService.calculateAverageConsultationFee();

        System.out.println("Average consultation fee: ₹" + average);
    }

    /**
     * Finds and displays the Doctor with the highest
     * consultation fee.
     */
    private static void findHighestFeeDoctor() {

        System.out.println(
                "\n--- HIGHEST CONSULTATION FEE ---"
        );

        Doctor doctor = doctorService.findHighestConsultationFeeDoctor();

        if (doctor == null) {
            System.out.println("No doctors found.");
            return;
        }

        printDoctor(doctor);
    }

    /**
     * Reads a specialization from the console.
     *
     * @return selected specialization
     */
    private static Specialization readSpecialization() {

        Specialization[] specializations = Specialization.values();

        System.out.println("\nSelect Specialization:");

        for (int i = 0; i < specializations.length; i++) {
            System.out.println(
                    (i + 1) + ". " + specializations[i]
            );
        }

        int choice = readInt("Enter choice: ");

        if (choice < 1 || choice > specializations.length) {

            throw new IllegalArgumentException("Invalid specialization choice.");
        }

        return specializations[choice - 1];
    }

    /**
     * Displays Doctor information.
     *
     * @param doctor doctor to display
     */
    private static void printDoctor(Doctor doctor) {

        System.out.println("-----------------------------");
        System.out.println("ID: " + doctor.getId());
        System.out.println("Name: " + doctor.getName());
        System.out.println("Age: " + doctor.getAge());
        System.out.println("Phone: " + doctor.getPhone());
        System.out.println("Email: " + doctor.getEmail());
        System.out.println("Specialization: " + doctor.getSpecialization());
        System.out.println("Consultation Fee: ₹" + doctor.getConsultationFee());
        System.out.println("Available: " + doctor.isAvailable());
        System.out.println("-----------------------------");
    }

    // ============================================================
    // PATIENT MANAGEMENT
    // ============================================================

    /**
     * Displays the Patient management menu.
     */
    private static void patientMenu() {

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("       PATIENT MANAGEMENT");
            System.out.println("=================================");
            System.out.println("1. Add Patient");
            System.out.println("2. Get All Patients");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Search Patient By ID");
            System.out.println("6. Search Patient By Name");
            System.out.println("7. Search Patient By Age");
            System.out.println("8. Back");
            System.out.println("=================================");

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addPatient();
                    break;

                case 2:
                    getAllPatients();
                    break;

                case 3:
                    updatePatient();
                    break;

                case 4:
                    deletePatient();
                    break;

                case 5:
                    searchPatientById();
                    break;

                case 6:
                    searchPatientByName();
                    break;

                case 7:
                    searchPatientByAge();
                    break;

                case 8:
                    running = false;
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }
    }

    /**
     * Adds a new Patient using console input.
     */
    private static void addPatient() {

        System.out.println("\n--- ADD PATIENT ---");

        long id = readLong("Enter patient ID: ");

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        int age = readInt("Enter age: ");

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter blood group: ");
        String bloodGroup = scanner.nextLine();

        System.out.print("Enter medical history: ");
        String medicalHistory = scanner.nextLine();

        Patient patient = new Patient(
                id,
                name,
                age,
                phone,
                email,
                bloodGroup,
                medicalHistory
        );

        try {

            patientService.addPatient(patient);

            System.out.println("Patient added successfully.");

        } catch (RuntimeException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all patients.
     */
    private static void getAllPatients() {

        System.out.println("\n--- ALL PATIENTS ---");

        List<Patient> patients =
                patientService.getAllPatients();

        if (patients.isEmpty()) {

            System.out.println("No patients found.");
            return;
        }

        for (Patient patient : patients) {
            printPatient(patient);
        }
    }

    /**
     * Updates an existing Patient.
     */
    private static void updatePatient() {

        System.out.println("\n--- UPDATE PATIENT ---");

        long id = readLong("Enter patient ID: ");

        try {

            Patient patient =
                    patientService.getPatientById(id);

            System.out.println(
                    "Current patient: " + patient.getName()
            );

            System.out.print("Enter new name: ");
            String name = scanner.nextLine();

            int age = readInt("Enter new age: ");

            System.out.print("Enter new phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            System.out.print("Enter new blood group: ");
            String bloodGroup = scanner.nextLine();

            System.out.print("Enter new medical history: ");
            String medicalHistory = scanner.nextLine();

            patient.setName(name);
            patient.setAge(age);
            patient.setPhone(phone);
            patient.setEmail(email);
            patient.setBloodGroup(bloodGroup);
            patient.setMedicalHistory(medicalHistory);

            patientService.updatePatient(patient);

            System.out.println(
                    "Patient updated successfully."
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Deletes a Patient by ID.
     */
    private static void deletePatient() {

        System.out.println("\n--- DELETE PATIENT ---");

        long id = readLong("Enter patient ID: ");

        try {

            patientService.deletePatient(id);

            System.out.println(
                    "Patient deleted successfully."
            );

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Searches for a Patient by ID.
     */
    private static void searchPatientById() {

        System.out.println(
                "\n--- SEARCH PATIENT BY ID ---"
        );

        long id = readLong("Enter patient ID: ");

        try {

            Patient patient =
                    patientService.searchPatient(id);

            printPatient(patient);

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Searches Patients by name.
     */
    private static void searchPatientByName() {

        System.out.println(
                "\n--- SEARCH PATIENT BY NAME ---"
        );

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        try {

            List<Patient> patients =
                    patientService.searchPatient(name);

            if (patients.isEmpty()) {

                System.out.println(
                        "No matching patients found."
                );

                return;
            }

            for (Patient patient : patients) {
                printPatient(patient);
            }

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Searches Patients by age.
     */
    private static void searchPatientByAge() {

        System.out.println(
                "\n--- SEARCH PATIENT BY AGE ---"
        );

        int age = readInt("Enter age: ");

        try {

            List<Patient> patients =
                    patientService.searchPatient(age);

            if (patients.isEmpty()) {

                System.out.println(
                        "No matching patients found."
                );

                return;
            }

            for (Patient patient : patients) {
                printPatient(patient);
            }

        } catch (RuntimeException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    /**
     * Displays Patient information.
     *
     * @param patient patient to display
     */
    private static void printPatient(Patient patient) {

        System.out.println("-----------------------------");
        System.out.println("ID: " + patient.getId());
        System.out.println("Name: " + patient.getName());
        System.out.println("Age: " + patient.getAge());
        System.out.println("Phone: " + patient.getPhone());
        System.out.println("Email: " + patient.getEmail());
        System.out.println(
                "Blood Group: " + patient.getBloodGroup()
        );
        System.out.println(
                "Medical History: " + patient.getMedicalHistory()
        );
        System.out.println("-----------------------------");
    }

    /**
     * Reads an integer from the console.
     *
     * @param message prompt displayed to the user
     * @return entered integer
     */
    private static int readInt(String message) {

        System.out.print(message);

        int value = scanner.nextInt();
        scanner.nextLine();

        return value;
    }

    /**
     * Reads a long value from the console.
     *
     * @param message prompt displayed to the user
     * @return entered long value
     */
    private static long readLong(String message) {

        System.out.print(message);

        long value = scanner.nextLong();
        scanner.nextLine();

        return value;
    }

    /**
     * Reads a double value from the console.
     *
     * @param message prompt displayed to the user
     * @return entered double value
     */
    private static double readDouble(String message) {

        System.out.print(message);

        double value = scanner.nextDouble();
        scanner.nextLine();

        return value;
    }

    /**
     * Reads a boolean value from the console.
     *
     * @param message prompt displayed to the user
     * @return entered boolean value
     */
    private static boolean readBoolean(String message) {

        System.out.print(message);

        boolean value = scanner.nextBoolean();
        scanner.nextLine();

        return value;
    }
}