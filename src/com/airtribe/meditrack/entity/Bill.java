package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.BillingType;
import com.airtribe.meditrack.interfaces.Payable;

public class Bill implements Payable {

    private long billId;
    private Appointment appointment;
    private BillingType billingType;
    private double amount;
    private boolean paid;

    public Bill(long billId,
                Appointment appointment,
                BillingType billingType,
                double amount) {

        this.billId = billId;
        this.appointment = appointment;
        this.billingType = billingType;
        this.amount = amount;
        this.paid = false;
    }

    public long getBillId() {
        return billId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void markAsPaid() {
        this.paid = true;
    }

    @Override
    public double calculateAmount() {
        return amount;
    }

    @Override
    public void processPayment() {
        if (!paid) {
            paid = true;
        }
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", appointmentId=" +
                (appointment != null
                        ? appointment.getAppointmentId()
                        : "null") +
                ", billingType=" + billingType +
                ", amount=" + amount +
                ", paid=" + paid +
                '}';
    }
}