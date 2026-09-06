package com.airtribe.meditrack.service;

import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BillingService {

    private final DataStore<Bill> billStore;
    private final AppointmentService appointmentService;

    public BillingService(
            DataStore<Bill> billStore,
            AppointmentService appointmentService) {

        Validator.requireNotNull(billStore, "Bill store");
        Validator.requireNotNull(
                appointmentService,
                "Appointment service"
        );

        this.billStore = billStore;
        this.appointmentService = appointmentService;
    }

    // ---------------------------------------------------------
    // CREATE BILL USING DOCTOR CONSULTATION FEE
    // ---------------------------------------------------------

    public Bill createConsultationBill(long appointmentId) {

        Appointment appointment =
                appointmentService.getAppointmentById(appointmentId);

        double amount =
                appointment.getDoctor().getConsultationFee();

        return createBill(
                appointmentId,
                BillingType.CONSULTATION,
                amount
        );
    }

    // ---------------------------------------------------------
    // CREATE BILL WITH CUSTOM AMOUNT
    // ---------------------------------------------------------

    public Bill createBill(
            long appointmentId,
            BillingType billingType,
            double amount) {

        Validator.requirePositive(
                appointmentId,
                "Appointment ID"
        );

        Validator.requireNotNull(
                billingType,
                "Billing type"
        );

        Validator.requirePositive(
                amount,
                "Bill amount"
        );

        Appointment appointment =
                appointmentService.getAppointmentById(
                        appointmentId
                );

        // One appointment should not accidentally
        // receive multiple bills of the same type.
        boolean alreadyExists = billStore.findAll()
                .stream()
                .anyMatch(bill ->
                        bill.getAppointment()
                                .getAppointmentId()
                                == appointmentId
                                && bill.getBillingType()
                                == billingType
                );

        if (alreadyExists) {
            throw new IllegalArgumentException(
                    "A bill of type " + billingType +
                            " already exists for appointment " +
                            appointmentId
            );
        }

        long billId = IdGenerator.nextBillId();

        Bill bill = new Bill(
                billId,
                appointment,
                billingType,
                amount
        );

        billStore.save(billId, bill);

        return bill;
    }

    // ---------------------------------------------------------
    // GET BILL
    // ---------------------------------------------------------

    public Bill getBillById(long billId) {

        Validator.requirePositive(
                billId,
                "Bill ID"
        );

        Bill bill = billStore.findById(billId);

        if (bill == null) {
            throw new IllegalArgumentException(
                    "Bill with ID " + billId +
                            " not found."
            );
        }

        return bill;
    }

    // ---------------------------------------------------------
    // GET ALL BILLS
    // ---------------------------------------------------------

    public List<Bill> getAllBills() {

        return billStore.findAll()
                .stream()
                .sorted(
                        Comparator.comparingLong(
                                Bill::getBillId
                        )
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // GET BILLS FOR APPOINTMENT
    // ---------------------------------------------------------

    public List<Bill> getBillsByAppointment(
            long appointmentId) {

        appointmentService.getAppointmentById(
                appointmentId
        );

        return billStore.findAll()
                .stream()
                .filter(bill ->
                        bill.getAppointment()
                                .getAppointmentId()
                                == appointmentId
                )
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // PROCESS PAYMENT
    // ---------------------------------------------------------

    public Bill processPayment(long billId) {

        Bill bill = getBillById(billId);

        if (bill.isPaid()) {
            throw new IllegalStateException(
                    "Bill " + billId +
                            " has already been paid."
            );
        }

        bill.processPayment();

        billStore.update(
                billId,
                bill
        );

        return bill;
    }

    // ---------------------------------------------------------
    // CALCULATE TAX
    // ---------------------------------------------------------

    public double calculateTax(double amount) {

        Validator.requirePositive(
                amount,
                "Amount"
        );

        return amount * Constants.TAX_RATE;
    }

    // ---------------------------------------------------------
    // CALCULATE TOTAL
    // ---------------------------------------------------------

    public double calculateTotal(double amount) {

        return amount + calculateTax(amount);
    }

    // ---------------------------------------------------------
    // CREATE IMMUTABLE BILL SUMMARY
    // ---------------------------------------------------------

    public BillSummary getBillSummary(long billId) {

        Bill bill = getBillById(billId);

        double baseAmount =
                bill.calculateAmount();

        double taxAmount =
                calculateTax(baseAmount);

        double totalAmount =
                calculateTotal(baseAmount);

        return new BillSummary(
                bill.getBillId(),
                bill.getAppointment().getAppointmentId(),
                bill.getBillingType(),
                baseAmount,
                taxAmount,
                totalAmount,
                bill.isPaid()
        );
    }

    // ---------------------------------------------------------
    // TOTAL REVENUE
    // ---------------------------------------------------------

    public double getTotalRevenue() {

        return billStore.findAll()
                .stream()
                .filter(Bill::isPaid)
                .mapToDouble(
                        bill -> calculateTotal(
                                bill.calculateAmount()
                        )
                )
                .sum();
    }

    // ---------------------------------------------------------
    // UNPAID BILLS
    // ---------------------------------------------------------

    public List<Bill> getUnpaidBills() {

        return billStore.findAll()
                .stream()
                .filter(bill -> !bill.isPaid())
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // BILL COUNT
    // ---------------------------------------------------------

    public int getBillCount() {
        return billStore.size();
    }
}