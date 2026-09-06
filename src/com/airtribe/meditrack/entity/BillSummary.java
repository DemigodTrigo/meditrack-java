package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.BillingType;

public final class BillSummary {

    private final long billId;
    private final long appointmentId;
    private final BillingType billingType;
    private final double baseAmount;
    private final double taxAmount;
    private final double totalAmount;
    private final boolean paid;

    public BillSummary(long billId,
                       long appointmentId,
                       BillingType billingType,
                       double baseAmount,
                       double taxAmount,
                       double totalAmount,
                       boolean paid) {

        this.billId = billId;
        this.appointmentId = appointmentId;
        this.billingType = billingType;
        this.baseAmount = baseAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paid = paid;
    }

    public long getBillId() {
        return billId;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public BillingType getBillingType() {
        return billingType;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "billId=" + billId +
                ", appointmentId=" + appointmentId +
                ", billingType=" + billingType +
                ", baseAmount=" + baseAmount +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", paid=" + paid +
                '}';
    }
}