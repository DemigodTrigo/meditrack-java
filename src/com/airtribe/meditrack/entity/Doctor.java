package com.airtribe.meditrack.entity;
import  com.airtribe.meditrack.constants.Specialization;
/**
 * Represents a doctor in the MediTrack system.
 *
 * <p>A Doctor is a specialized type of Person and contains
 * doctor-specific information such as specialization,
 * consultation fee, and availability.</p>
 */

public class Doctor extends Person {
    private Specialization specialization;

    private double consultationFee;

    private boolean available;
    /**
     * Creates a Doctor with the specified personal and professional details.
     */
    public Doctor(long id, String name, int age, String phone,String email, Specialization specialization, double consultationFee, boolean available)
    {
        super(id, name, age, phone,email);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.available = available;

    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Compares this doctor with another object based on the doctor's unique ID.
     *
     * @param o object to compare with this doctor
     * @return true if both objects have the same doctor ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor doctor)) return false;
        return getId() == doctor.getId();
    }

    /**
     * Returns a hash code based on the doctor's unique ID.
     *
     * @return hash code derived from the doctor ID
     */
    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }
}
