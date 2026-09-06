package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.AppointmentStatus;
import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final DataStore<Doctor> doctorStore =
            new DataStore<>();

    private static final DataStore<Patient> patientStore =
            new DataStore<>();

    private static final DataStore<Appointment> appointmentStore =
            new DataStore<>();

    private static final DataStore<Bill> billStore =
            new DataStore<>();

    private static final DoctorService doctorService =
            new DoctorService(doctorStore);

    private static final PatientService patientService =
            new PatientService(patientStore);

    private static final AppointmentService appointmentService =
            new AppointmentService(
                    appointmentStore,
                    doctorService,
                    patientService
            );

    private static final BillingService billingService =
            new BillingService(
                    billStore,
                    appointmentService
            );

    public static void main(String[] args) {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("              MEDI TRACK");
        System.out.println("       Hospital Management System");
        System.out.println("==========================================");

        boolean running = true;

        while (running) {

            printMainMenu();

            int choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        doctorMenu();
                        break;

                    case 2:
                        patientMenu();
                        break;

                    case 3:
                        appointmentMenu();
                        break;

                    case 4:
                        billingMenu();
                        break;

                    case 5:
                        showDashboard();
                        break;

                    case 0:
                        running = false;
                        System.out.println(
                                "\nThank you for using MediTrack."
                        );
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please try again."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "\nOperation failed: " +
                                e.getMessage()
                );
            }
        }

        scanner.close();
    }

    // =========================================================
    // MAIN MENU
    // =========================================================

    private static void printMainMenu() {

        System.out.println();
        System.out.println("------------------------------------------");
        System.out.println("                MAIN MENU");
        System.out.println("------------------------------------------");
        System.out.println("1. Doctor Management");
        System.out.println("2. Patient Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing Management");
        System.out.println("5. Dashboard");
        System.out.println("0. Exit");
        System.out.println("------------------------------------------");
    }

    // =========================================================
    // DOCTOR MENU
    // =========================================================

    private static void doctorMenu() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("------------------------------------------");
            System.out.println("           DOCTOR MANAGEMENT");
            System.out.println("------------------------------------------");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctor");
            System.out.println("3. View All Doctors");
            System.out.println("4. Search Doctor");
            System.out.println("5. Update Doctor");
            System.out.println("6. Delete Doctor");
            System.out.println("7. Doctor Statistics");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        addDoctor();
                        break;

                    case 2:
                        viewDoctor();
                        break;

                    case 3:
                        viewAllDoctors();
                        break;

                    case 4:
                        searchDoctor();
                        break;

                    case 5:
                        updateDoctor();
                        break;

                    case 6:
                        deleteDoctor();
                        break;

                    case 7:
                        doctorStatistics();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: " +
                                e.getMessage()
                );
            }
        }
    }

    private static void addDoctor() {

        System.out.println("\n--- ADD DOCTOR ---");

        long id = readLong("Doctor ID: ");
        String name = readString("Name: ");
        int age = readInt("Age: ");
        String phone = readString("Phone: ");
        String email = readString("Email: ");

        Specialization specialization =
                chooseSpecialization();

        double fee =
                readDouble("Consultation fee: ");

        Doctor doctor = new Doctor(
                id,
                name,
                age,
                phone,
                email,
                specialization,
                fee,
                true
        );

        doctorService.addDoctor(doctor);

        System.out.println(
                "Doctor added successfully."
        );
    }

    private static void viewDoctor() {

        long id = readLong("Doctor ID: ");

        Doctor doctor =
                doctorService.getDoctorById(id);

        System.out.println("\n" + doctor);
    }

    private static void viewAllDoctors() {

        List<Doctor> doctors =
                doctorService.getAllDoctors();

        if (doctors.isEmpty()) {

            System.out.println(
                    "No doctors found."
            );

            return;
        }

        System.out.println("\n--- DOCTORS ---");

        for (Doctor doctor : doctors) {
            System.out.println(doctor);
        }
    }

    private static void searchDoctor() {

        String text =
                readString("Search doctor: ");

        List<Doctor> doctors =
                doctorService.searchDoctor(text);

        if (doctors.isEmpty()) {

            System.out.println(
                    "No doctors found."
            );

            return;
        }

        for (Doctor doctor : doctors) {
            System.out.println(doctor);
        }
    }

    private static void updateDoctor() {

        long id =
                readLong("Doctor ID to update: ");

        Doctor doctor =
                doctorService.getDoctorById(id);

        System.out.println(
                "Current doctor: " + doctor
        );

        String name =
                readString("New name: ");

        int age =
                readInt("New age: ");

        String phone =
                readString("New phone: ");

        String email =
                readString("New email: ");

        Specialization specialization =
                chooseSpecialization();

        double fee =
                readDouble("New consultation fee: ");

        doctor.setName(name);
        doctor.setAge(age);
        doctor.setPhone(phone);
        doctor.setEmail(email);
        doctor.setSpecialization(specialization);
        doctor.setConsultationFee(fee);

        doctorService.updateDoctor(doctor);

        System.out.println(
                "Doctor updated successfully."
        );
    }

    private static void deleteDoctor() {

        long id =
                readLong("Doctor ID to delete: ");

        doctorService.deleteDoctor(id);

        System.out.println(
                "Doctor deleted successfully."
        );
    }

    private static void doctorStatistics() {

        System.out.println(
                "Total doctors: " +
                        doctorService.getAllDoctors().size()
        );

        System.out.println(
                "Average consultation fee: ₹" +
                        doctorService.calculateAverageConsultationFee()
        );

        Doctor highest =
                doctorService.findHighestConsultationFeeDoctor();

        if (highest != null) {

            System.out.println(
                    "Highest fee doctor: " +
                            highest.getName() +
                            " - ₹" +
                            highest.getConsultationFee()
            );
        }
    }

    // =========================================================
    // PATIENT MENU
    // =========================================================

    private static void patientMenu() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("------------------------------------------");
            System.out.println("           PATIENT MANAGEMENT");
            System.out.println("------------------------------------------");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patient");
            System.out.println("3. View All Patients");
            System.out.println("4. Search Patient");
            System.out.println("5. Update Patient");
            System.out.println("6. Delete Patient");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice =
                    readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        addPatient();
                        break;

                    case 2:
                        viewPatient();
                        break;

                    case 3:
                        viewAllPatients();
                        break;

                    case 4:
                        searchPatient();
                        break;

                    case 5:
                        updatePatient();
                        break;

                    case 6:
                        deletePatient();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: " +
                                e.getMessage()
                );
            }
        }
    }

    private static void addPatient() {

        System.out.println("\n--- ADD PATIENT ---");

        long id =
                readLong("Patient ID: ");

        String name =
                readString("Name: ");

        int age =
                readInt("Age: ");

        String phone =
                readString("Phone: ");

        String email =
                readString("Email: ");

        String bloodGroup =
                readString("Blood group: ");

        String medicalHistory =
                readString("Medical history: ");

        Patient patient = new Patient(
                id,
                name,
                age,
                phone,
                email,
                bloodGroup,
                medicalHistory
        );

        patientService.addPatient(patient);

        System.out.println(
                "Patient added successfully."
        );
    }

    private static void viewPatient() {

        long id =
                readLong("Patient ID: ");

        Patient patient =
                patientService.getPatientById(id);

        System.out.println("\n" + patient);
    }

    private static void viewAllPatients() {

        List<Patient> patients =
                patientService.getAllPatients();

        if (patients.isEmpty()) {

            System.out.println(
                    "No patients found."
            );

            return;
        }

        System.out.println("\n--- PATIENTS ---");

        for (Patient patient : patients) {
            System.out.println(patient);
        }
    }

    private static void searchPatient() {

        String text =
                readString("Search patient: ");

        List<Patient> patients =
                patientService.searchPatients(text);

        if (patients.isEmpty()) {

            System.out.println(
                    "No patients found."
            );

            return;
        }

        for (Patient patient : patients) {
            System.out.println(patient);
        }
    }

    private static void updatePatient() {

        long id =
                readLong("Patient ID to update: ");

        Patient patient =
                patientService.getPatientById(id);

        String name =
                readString("New name: ");

        int age =
                readInt("New age: ");

        String phone =
                readString("New phone: ");

        String email =
                readString("New email: ");

        String bloodGroup =
                readString("New blood group: ");

        String medicalHistory =
                readString("New medical history: ");

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
    }

    private static void deletePatient() {

        long id =
                readLong("Patient ID to delete: ");

        patientService.deletePatient(id);

        System.out.println(
                "Patient deleted successfully."
        );
    }

    // =========================================================
    // APPOINTMENT MENU
    // =========================================================

    private static void appointmentMenu() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("------------------------------------------");
            System.out.println("         APPOINTMENT MANAGEMENT");
            System.out.println("------------------------------------------");
            System.out.println("1. Create Appointment");
            System.out.println("2. View Appointment");
            System.out.println("3. View All Appointments");
            System.out.println("4. View Doctor Appointments");
            System.out.println("5. View Patient Appointments");
            System.out.println("6. Confirm Appointment");
            System.out.println("7. Cancel Appointment");
            System.out.println("8. Update Appointment");
            System.out.println("9. Upcoming Appointments");
            System.out.println("10. Delete Appointment");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice =
                    readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        createAppointment();
                        break;

                    case 2:
                        viewAppointment();
                        break;

                    case 3:
                        viewAllAppointments();
                        break;

                    case 4:
                        viewDoctorAppointments();
                        break;

                    case 5:
                        viewPatientAppointments();
                        break;

                    case 6:
                        confirmAppointment();
                        break;

                    case 7:
                        cancelAppointment();
                        break;

                    case 8:
                        updateAppointment();
                        break;

                    case 9:
                        viewUpcomingAppointments();
                        break;

                    case 10:
                        deleteAppointment();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: " +
                                e.getMessage()
                );
            }
        }
    }

    private static void createAppointment() {

        System.out.println("\n--- CREATE APPOINTMENT ---");

        long doctorId =
                readLong("Doctor ID: ");

        long patientId =
                readLong("Patient ID: ");

        String dateTime =
                readString(
                        "Date/time (dd-MM-yyyy HH:mm): "
                );

        LocalDateTime appointmentTime =
                DateUtil.parse(dateTime);

        String reason =
                readString("Reason: ");

        Appointment appointment =
                appointmentService.createAppointment(
                        doctorId,
                        patientId,
                        appointmentTime,
                        reason
                );

        System.out.println(
                "Appointment created successfully."
        );

        System.out.println(appointment);
    }

    private static void viewAppointment() {

        long id =
                readLong("Appointment ID: ");

        Appointment appointment =
                appointmentService.getAppointmentById(id);

        System.out.println("\n" + appointment);
    }

    private static void viewAllAppointments() {

        List<Appointment> appointments =
                appointmentService.getAllAppointments();

        if (appointments.isEmpty()) {

            System.out.println(
                    "No appointments found."
            );

            return;
        }

        for (Appointment appointment :
                appointments) {

            System.out.println(appointment);
        }
    }

    private static void viewDoctorAppointments() {

        long doctorId =
                readLong("Doctor ID: ");

        List<Appointment> appointments =
                appointmentService
                        .getAppointmentsByDoctor(doctorId);

        printAppointments(appointments);
    }

    private static void viewPatientAppointments() {

        long patientId =
                readLong("Patient ID: ");

        List<Appointment> appointments =
                appointmentService
                        .getAppointmentsByPatient(patientId);

        printAppointments(appointments);
    }

    private static void confirmAppointment() {

        long id =
                readLong("Appointment ID: ");

        Appointment appointment =
                appointmentService
                        .confirmAppointment(id);

        System.out.println(
                "Appointment confirmed."
        );

        System.out.println(appointment);
    }

    private static void cancelAppointment() {

        long id =
                readLong("Appointment ID: ");

        Appointment appointment =
                appointmentService
                        .cancelAppointment(id);

        System.out.println(
                "Appointment cancelled."
        );

        System.out.println(appointment);
    }

    private static void updateAppointment() {

        long id =
                readLong("Appointment ID: ");

        String dateTime =
                readString(
                        "New date/time (dd-MM-yyyy HH:mm): "
                );

        LocalDateTime newDateTime =
                DateUtil.parse(dateTime);

        String reason =
                readString("New reason: ");

        Appointment appointment =
                appointmentService.updateAppointment(
                        id,
                        newDateTime,
                        reason
                );

        System.out.println(
                "Appointment updated."
        );

        System.out.println(appointment);
    }

    private static void viewUpcomingAppointments() {

        List<Appointment> appointments =
                appointmentService
                        .getUpcomingAppointments();

        printAppointments(appointments);
    }

    private static void deleteAppointment() {

        long id =
                readLong("Appointment ID: ");

        appointmentService.deleteAppointment(id);

        System.out.println(
                "Appointment deleted."
        );
    }

    // =========================================================
    // BILLING MENU
    // =========================================================

    private static void billingMenu() {

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("------------------------------------------");
            System.out.println("            BILLING MANAGEMENT");
            System.out.println("------------------------------------------");
            System.out.println("1. Create Consultation Bill");
            System.out.println("2. Create Custom Bill");
            System.out.println("3. View Bill");
            System.out.println("4. View All Bills");
            System.out.println("5. View Appointment Bills");
            System.out.println("6. Process Payment");
            System.out.println("7. View Bill Summary");
            System.out.println("8. View Unpaid Bills");
            System.out.println("9. View Total Revenue");
            System.out.println("0. Back");
            System.out.println("------------------------------------------");

            int choice =
                    readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1:
                        createConsultationBill();
                        break;

                    case 2:
                        createCustomBill();
                        break;

                    case 3:
                        viewBill();
                        break;

                    case 4:
                        viewAllBills();
                        break;

                    case 5:
                        viewAppointmentBills();
                        break;

                    case 6:
                        processPayment();
                        break;

                    case 7:
                        viewBillSummary();
                        break;

                    case 8:
                        viewUnpaidBills();
                        break;

                    case 9:
                        viewRevenue();
                        break;

                    case 0:
                        back = true;
                        break;

                    default:
                        System.out.println(
                                "Invalid choice."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "Operation failed: " +
                                e.getMessage()
                );
            }
        }
    }

    private static void createConsultationBill() {

        long appointmentId =
                readLong("Appointment ID: ");

        Bill bill =
                billingService
                        .createConsultationBill(
                                appointmentId
                        );

        System.out.println(
                "Consultation bill created."
        );

        System.out.println(bill);
    }

    private static void createCustomBill() {

        long appointmentId =
                readLong("Appointment ID: ");

        BillingType type =
                chooseBillingType();

        double amount =
                readDouble("Amount: ");

        Bill bill =
                billingService.createBill(
                        appointmentId,
                        type,
                        amount
                );

        System.out.println(
                "Bill created successfully."
        );

        System.out.println(bill);
    }

    private static void viewBill() {

        long billId =
                readLong("Bill ID: ");

        Bill bill =
                billingService.getBillById(billId);

        System.out.println(bill);
    }

    private static void viewAllBills() {

        List<Bill> bills =
                billingService.getAllBills();

        if (bills.isEmpty()) {

            System.out.println(
                    "No bills found."
            );

            return;
        }

        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    private static void viewAppointmentBills() {

        long appointmentId =
                readLong("Appointment ID: ");

        List<Bill> bills =
                billingService
                        .getBillsByAppointment(
                                appointmentId
                        );

        if (bills.isEmpty()) {

            System.out.println(
                    "No bills found."
            );

            return;
        }

        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    private static void processPayment() {

        long billId =
                readLong("Bill ID: ");

        Bill bill =
                billingService
                        .processPayment(billId);

        System.out.println(
                "Payment processed successfully."
        );

        System.out.println(bill);
    }

    private static void viewBillSummary() {

        long billId =
                readLong("Bill ID: ");

        BillSummary summary =
                billingService
                        .getBillSummary(billId);

        System.out.println(
                "\n========== BILL SUMMARY =========="
        );

        System.out.println(
                "Bill ID       : " +
                        summary.getBillId()
        );

        System.out.println(
                "Appointment ID : " +
                        summary.getAppointmentId()
        );

        System.out.println(
                "Billing Type   : " +
                        summary.getBillingType()
        );

        System.out.println(
                "Base Amount    : ₹" +
                        summary.getBaseAmount()
        );

        System.out.println(
                "Tax            : ₹" +
                        summary.getTaxAmount()
        );

        System.out.println(
                "Total Amount   : ₹" +
                        summary.getTotalAmount()
        );

        System.out.println(
                "Paid           : " +
                        summary.isPaid()
        );

        System.out.println(
                "=================================="
        );
    }

    private static void viewUnpaidBills() {

        List<Bill> bills =
                billingService.getUnpaidBills();

        if (bills.isEmpty()) {

            System.out.println(
                    "No unpaid bills."
            );

            return;
        }

        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    private static void viewRevenue() {

        double revenue =
                billingService.getTotalRevenue();

        System.out.println(
                "Total revenue: ₹" +
                        revenue
        );
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private static void showDashboard() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("              MEDI TRACK");
        System.out.println("               DASHBOARD");
        System.out.println("==========================================");

        System.out.println(
                "Doctors       : " +
                        doctorService.getAllDoctors().size()
        );

        System.out.println(
                "Patients      : " +
                        patientService.getPatientCount()
        );

        System.out.println(
                "Appointments  : " +
                        appointmentService.getAppointmentCount()
        );

        System.out.println(
                "Bills         : " +
                        billingService.getBillCount()
        );

        System.out.println(
                "Upcoming      : " +
                        appointmentService
                                .getUpcomingAppointments()
                                .size()
        );

        System.out.println(
                "Unpaid Bills  : " +
                        billingService
                                .getUnpaidBills()
                                .size()
        );

        System.out.println(
                "Revenue       : ₹" +
                        billingService.getTotalRevenue()
        );

        System.out.println("==========================================");
    }

    // =========================================================
    // DISPLAY HELPERS
    // =========================================================

    private static void printAppointments(
            List<Appointment> appointments) {

        if (appointments.isEmpty()) {

            System.out.println(
                    "No appointments found."
            );

            return;
        }

        for (Appointment appointment :
                appointments) {

            System.out.println(appointment);
        }
    }

    // =========================================================
    // INPUT HELPERS
    // =========================================================

    private static String readString(String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static long readLong(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Long.parseLong(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static double readDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Double.parseDouble(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid amount."
                );
            }
        }
    }

    // =========================================================
    // ENUM HELPERS
    // =========================================================

    private static Specialization chooseSpecialization() {

        Specialization[] values =
                Specialization.values();

        System.out.println("\nSelect specialization:");

        for (int i = 0; i < values.length; i++) {

            System.out.println(
                    (i + 1) + ". " + values[i]
            );
        }

        while (true) {

            int choice =
                    readInt("Choice: ");

            if (choice >= 1 &&
                    choice <= values.length) {

                return values[choice - 1];
            }

            System.out.println(
                    "Invalid specialization."
            );
        }
    }

    private static BillingType chooseBillingType() {

        BillingType[] values =
                BillingType.values();

        System.out.println("\nSelect billing type:");

        for (int i = 0; i < values.length; i++) {

            System.out.println(
                    (i + 1) + ". " + values[i]
            );
        }

        while (true) {

            int choice =
                    readInt("Choice: ");

            if (choice >= 1 &&
                    choice <= values.length) {

                return values[choice - 1];
            }

            System.out.println(
                    "Invalid billing type."
            );
        }
    }
}