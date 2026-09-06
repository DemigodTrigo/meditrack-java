package com.airtribe.meditrack.test;

public class TestRunner {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("          MEDI TRACK TEST RUNNER");
        System.out.println("========================================");

        try {

            System.out.println("\n--- Running Doctor Tests ---");
            DoctorTest.run();

            System.out.println("\n--- Running Patient Tests ---");
            PatientTest.run();

            System.out.println("\n--- Running Appointment Tests ---");
            AppointmentTestRunner.main(new String[0]);

            System.out.println("\n--- Running Billing Tests ---");
            BillingTest.run();

            System.out.println("\n========================================");
            System.out.println("       ALL TESTS COMPLETED");
            System.out.println("========================================");

        } catch (Exception e) {

            System.out.println("\n========================================");
            System.out.println("             TEST FAILED");
            System.out.println("========================================");

            System.out.println("Error: " + e.getMessage());

            e.printStackTrace();
        }
    }
}