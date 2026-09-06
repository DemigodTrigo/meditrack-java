package com.airtribe.meditrack.test;

import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.constants.Specialization;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.BillingService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;

import java.time.LocalDateTime;

public class BillingTest {

    public static void run() {

        System.out.println("\n===== BILLING TEST =====");

        // -------------------------------------------------
        // Setup
        // -------------------------------------------------

        DataStore<Doctor> doctorStore = new DataStore<>();
        DataStore<Patient> patientStore = new DataStore<>();
        DataStore<Appointment> appointmentStore = new DataStore<>();
        DataStore<Bill> billStore = new DataStore<>();

        DoctorService doctorService =
                new DoctorService(doctorStore);

        PatientService patientService =
                new PatientService(patientStore);

        AppointmentService appointmentService =
                new AppointmentService(
                        appointmentStore,
                        doctorService,
                        patientService
                );

        BillingService billingService =
                new BillingService(
                        billStore,
                        appointmentService
                );

        // -------------------------------------------------
        // Create doctor
        // -------------------------------------------------

        Doctor doctor = new Doctor(
                10,
                "Dr. Billing",
                40,
                "9000000010",
                "billing@meditrack.com",
                Specialization.CARDIOLOGIST,
                800.0,
                true
        );

        doctorService.addDoctor(doctor);

        // -------------------------------------------------
        // Create patient
        // -------------------------------------------------

        Patient patient = new Patient(
                110,
                "Billing Patient",
                30,
                "9111111110",
                "patient@meditrack.com",
                "O+",
                "No known allergies"
        );

        patientService.addPatient(patient);

        // -------------------------------------------------
        // Create appointment
        // -------------------------------------------------

        LocalDateTime appointmentTime =
                LocalDateTime.now()
                        .plusDays(2)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);

        Appointment appointment =
                appointmentService.createAppointment(
                        doctor.getId(),
                        patient.getId(),
                        appointmentTime,
                        "Cardiac consultation"
                );

        System.out.println(
                "Appointment created: " +
                        appointment
        );

        // -------------------------------------------------
        // Create consultation bill
        // -------------------------------------------------

        Bill bill =
                billingService.createConsultationBill(
                        appointment.getAppointmentId()
                );

        System.out.println(
                "Bill created: " +
                        bill
        );

        // -------------------------------------------------
        // Check amount
        // -------------------------------------------------

        if (bill.getAmount() == 800.0) {
            System.out.println(
                    "Amount test passed."
            );
        } else {
            System.out.println(
                    "Amount test failed."
            );
        }

        // -------------------------------------------------
        // Tax calculation
        // -------------------------------------------------

        double tax =
                billingService.calculateTax(800.0);

        System.out.println(
                "Tax on 800 = " + tax
        );

        // -------------------------------------------------
        // Total calculation
        // -------------------------------------------------

        double total =
                billingService.calculateTotal(800.0);

        System.out.println(
                "Total amount = " + total
        );

        // -------------------------------------------------
        // Bill summary
        // -------------------------------------------------

        BillSummary summary =
                billingService.getBillSummary(
                        bill.getBillId()
                );

        System.out.println(
                "Bill summary: " +
                        summary
        );

        // -------------------------------------------------
        // Payment
        // -------------------------------------------------

        billingService.processPayment(
                bill.getBillId()
        );

        if (bill.isPaid()) {
            System.out.println(
                    "Payment test passed."
            );
        } else {
            System.out.println(
                    "Payment test failed."
            );
        }

        // -------------------------------------------------
        // Revenue
        // -------------------------------------------------

        double revenue =
                billingService.getTotalRevenue();

        System.out.println(
                "Total revenue = " + revenue
        );

        // -------------------------------------------------
        // Unpaid bills
        // -------------------------------------------------

        int unpaidBills =
                billingService
                        .getUnpaidBills()
                        .size();

        System.out.println(
                "Unpaid bills = " +
                        unpaidBills
        );

        System.out.println(
                "===== BILLING TEST COMPLETED ====="
        );
    }

    public static void main(String[] args) {
        run();
    }
}