package com.airtribe.meditrack.entity;

public class Patient extends Person {

    private String bloodGroup;
    private String medicalHistory;

    public Patient(long id,
                   String name,
                   int age,
                   String phone,
                   String email,
                   String bloodGroup,
                   String medicalHistory) {

        super(id, name, age, phone, email);

        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", age=" + getAge() +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", medicalHistory='" + medicalHistory + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Patient)) {
            return false;
        }

        Patient other = (Patient) obj;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }
}