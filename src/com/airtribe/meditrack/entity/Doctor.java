package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Specialization;

public class Doctor extends Person {

    private Specialization specialization;
    private double consultationFee;
    private boolean available;

    public Doctor(long id,
                  String name,
                  int age,
                  String phone,
                  String email,
                  Specialization specialization,
                  double consultationFee,
                  boolean available) {

        super(id, name, age, phone, email);
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

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", age=" + getAge() +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", specialization=" + specialization +
                ", consultationFee=" + consultationFee +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Doctor)) {
            return false;
        }

        Doctor other = (Doctor) obj;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }
}